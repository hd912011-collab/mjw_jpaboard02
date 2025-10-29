package org.mjw.jpaboard02.controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.mjw.jpaboard02.dto.BoardDTO;
import org.mjw.jpaboard02.dto.BoardListReplyCountDTO;
import org.mjw.jpaboard02.dto.PageRequestDTO;
import org.mjw.jpaboard02.dto.PageResponseDTO;
import org.mjw.jpaboard02.dto.upload.UploadFileDTO;
import org.mjw.jpaboard02.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        List<String> strFileNames=null;
        if(uploadFileDTO.getFiles()!=null && !uploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")){
            strFileNames=fileUpload(uploadFileDTO);
        }
        boardDTO.setFilenames(strFileNames);
        Long bno = boardService.insertBoard(boardDTO);
        log.info("board inserted success: bno="+bno);
        return "redirect:/board/list";
    }

    private List<String> fileUpload(UploadFileDTO uploadFileDTO) {
        List<String> list=new ArrayList<>();
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
                list.add(uuid+"_"+originalFileName);
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
    public String modifyBoard(BoardDTO boardDTO, RedirectAttributes redirectAttributes){
        log.info("modifyBoard"+boardDTO);
        boardService.updateBoard(boardDTO);
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        redirectAttributes.addAttribute("mode", 1);
        return "redirect:/board/read";
    }
    @GetMapping("remove")
    public String removeBoard(Long bno){
        log.info("removeBoard");
        boardService.deleteBoard(bno);
        return "redirect:/board/list";
    }
}
