package org.mjw.jpaboard02.controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.mjw.jpaboard02.dto.BoardDTO;
import org.mjw.jpaboard02.dto.upload.UploadFileDTO;
import org.mjw.jpaboard02.dto.upload.UploadResultDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@Log4j2
@RequestMapping("/upload")
public class UpDownController {
    @Value("${org.mjw.jpaboard02.upload.path}")
    private String uploadPath;

    @GetMapping("uploadForm")
    public void uploadForm(){
        log.info("uploadForm");
    }
    @PostMapping("uploadPro")
    public void upload(UploadFileDTO uploadFileDTO, BoardDTO boardDTO, Model model) {
        //log.info("upload"+uploadFileDTO);
        log.info("upload"+boardDTO);
        List<UploadResultDTO> list=new ArrayList<>();
        if(uploadFileDTO.getFiles() !=null){
            uploadFileDTO.getFiles().forEach(multiFile -> {
                String originalFileName=multiFile.getOriginalFilename();
                log.info("originalFileName:"+originalFileName);
                String uuid= UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath,uuid+"_"+originalFileName);
                boolean image=false;
                try {
                    multiFile.transferTo(savePath);
                    if(Files.probeContentType(savePath).startsWith("image")){
                        image=true;
                        File thumbnail=new File(uploadPath,"s_"+uuid+"_"+originalFileName);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbnail, 200, 200);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                list.add(UploadResultDTO.builder()
                        .uuid(uuid)
                        .fileName(originalFileName)
                        .image(image)
                        .build());
            });
            model.addAttribute("fileList",list);
            model.addAttribute("boardDTO",boardDTO);
        }
    }
    @GetMapping("view/{filename}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable("filename") String filename){
        Resource resource=new FileSystemResource(uploadPath+File.separator+filename);
        String resourceName=resource.getFilename();
        log.info("resourceName:"+resourceName);
        HttpHeaders headers=new HttpHeaders();
        try{
            headers.add("Content-Type",Files.probeContentType(resource.getFile().toPath()));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(resource);
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }
    @GetMapping("remove")
    public String removeFile(@RequestParam("filename") String filename){
        Resource resource=new FileSystemResource(uploadPath+File.separator+filename);
        String resourceName=resource.getFilename();
        Map<String, Boolean> resultMap=new HashMap<>();
        boolean removed=false;
        try{
            String contentType=Files.probeContentType(resource.getFile().toPath());
            removed=resource.getFile().delete();
            if(contentType.startsWith("image")){
                String fileName1=filename.replace("s_","");
                File originalFile=new File(uploadPath+File.separator+fileName1);
                originalFile.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultMap.put("removed", removed);
        return "/upload/uploadForm";
    }
}

