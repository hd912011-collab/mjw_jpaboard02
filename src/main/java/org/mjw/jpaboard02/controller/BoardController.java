package org.mjw.jpaboard02.controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.mjw.jpaboard02.dto.*;
import org.mjw.jpaboard02.dto.upload.UploadFileDTO;
import org.mjw.jpaboard02.dto.upload.UploadResultDTO;
import org.mjw.jpaboard02.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@Log4j2
@RequestMapping("/board")
public class BoardController {
    @Value("${org.mjw.jpaboard02.upload.path}")
    private String uploadPath;

    @Autowired
    BoardService boardService;

    @GetMapping("register")
    public void registerGet(){
        log.info("registerGet");
    }

    @PostMapping("register")
    public String registerPost(UploadFileDTO uploadFileDTO, BoardDTO boardDTO){
        List<BoardImageDTO> imageDTOS=null;
        if(uploadFileDTO.getFiles()!=null && !uploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")){
            imageDTOS=fileUpload(uploadFileDTO);
        }
        boardDTO.setBoardImageDTOS(imageDTOS);
        Long bno = boardService.insertBoard(boardDTO);
        log.info("board inserted success: bno="+bno);
        return "redirect:/board/list";
    }

    private List<BoardImageDTO> fileUpload(UploadFileDTO uploadFileDTO) {
        List<BoardImageDTO> list=new ArrayList<>();
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
                BoardImageDTO boardImageDTO=BoardImageDTO.builder()
                        .uuid(uuid)
                        .filename(originalFileName)
                        .image(image)
                        .build();
                list.add(boardImageDTO);
                //list.add(uuid+"_"+originalFileName);
            });
        }
        return list;
    }

    @GetMapping("/list")
    public void replyCountList(PageRequestDTO pageRequestDTO, Model model){
        log.info("replyCountList");
        PageResponseDTO<BoardListReplyCountDTO> responseDTO=boardService.getListReplyCount(pageRequestDTO);
        model.addAttribute("responseDTO",responseDTO);
    }

//    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO<BoardDTO> responseDTO=boardService.getList(pageRequestDTO);
        model.addAttribute("responseDTO",responseDTO);
        model.addAttribute("pageRequestDTO",pageRequestDTO);
    }
    //@GetMapping("list")
    public void list(Model model){
        log.info("list");
        model.addAttribute("boards", boardService.findAllBoards());
    }
    @GetMapping({"read", "modify"})
    public void readBoard(Long bno, Integer mode, PageRequestDTO PageRequestDTO, Model model){
        log.info("readBoard");
        model.addAttribute("PageRequestDTO",PageRequestDTO);
        model.addAttribute("board", boardService.findBoardById(bno, mode));
    }
    @PostMapping("modify")
    public String modifyBoard(UploadFileDTO uploadFileDTO, BoardDTO boardDTO, RedirectAttributes redirectAttributes){
        log.info("modifyBoard"+boardDTO);
        List<BoardImageDTO> imageDTOS=null; //새파일 정보 담음
        if(uploadFileDTO.getFiles()!=null && !uploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")){ //기존의 파일 삭제
            BoardDTO boardDTO1=boardService.findBoardById(boardDTO.getBno(),2);
            List<BoardImageDTO> imageDTOS1=boardDTO1.getBoardImageDTOS();
            if(imageDTOS1!=null && !imageDTOS1.isEmpty()){
                removeFile(imageDTOS1);//기존파일 삭제
            }
            //새로 업로드처리
            imageDTOS=fileUpload(uploadFileDTO);
        }
        boardDTO.setBoardImageDTOS(imageDTOS);
        boardService.updateBoard(boardDTO);
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        redirectAttributes.addAttribute("mode", 1);
        return "redirect:/board/read";
    }
    @GetMapping("remove")
    public String removeBoard(Long bno){
        log.info("removeBoard");
        BoardDTO boardDTO=boardService.findBoardById(bno,2);
        List<BoardImageDTO> imageDTOS=boardDTO.getBoardImageDTOS();
        if(imageDTOS!=null && !imageDTOS.isEmpty()){
            removeFile(imageDTOS);
        }
        boardService.deleteBoard(bno);
        return "redirect:/board/list";
    }
    public void removeFile(List<BoardImageDTO> imageDTOS){
        for(BoardImageDTO boardImageDTO:imageDTOS){
            String filename=boardImageDTO.getUuid()+"_"+boardImageDTO.getFilename();
            Resource resource=new FileSystemResource(
                    uploadPath+File.separator+filename);
            String resourceName=resource.getFilename();
            boolean removed=false;
            try{
                String contentType=Files.probeContentType(resource.getFile().toPath());
                removed=resource.getFile().delete(); // 원본 파일 삭제
                if(contentType.startsWith("image")){
                    String fileName1="s_"+filename;
                    File thumFile=new File(uploadPath+File.separator+fileName1);
                    thumFile.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
