package org.mjw.jpaboard02.controller;


import lombok.extern.log4j.Log4j2;
import org.mjw.jpaboard02.dto.PageRequestDTO;
import org.mjw.jpaboard02.dto.PageResponseDTO;
import org.mjw.jpaboard02.dto.ReplyDTO;
import org.mjw.jpaboard02.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/replies")
public class ReplyController {
    @Autowired
    private ReplyService replyService;

    @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> register(@RequestBody ReplyDTO replyDTO){
        log.debug(replyDTO.toString());
        Map<String,Long> map = new HashMap<>();
        Long rno=replyService.insertReply(replyDTO);
        map.put("rno",rno);
        return map;
    }

    @GetMapping("/list/{bno}")
    public PageResponseDTO<ReplyDTO> getReplies(@PathVariable("bno") Long bno,
                                                PageRequestDTO pageRequestDTO){
        PageResponseDTO<ReplyDTO> responseDTO=replyService.getListOfBoard(bno,pageRequestDTO);
        return responseDTO;
    }

    @GetMapping("/{rno}")
    public ReplyDTO read(@PathVariable("rno") Long rno){
        log.info("read"+rno);
        ReplyDTO replyDTO=replyService.findById(rno);
        return replyDTO;
    }

    @DeleteMapping("/{rno}")
    public Map<String,Long> remove(@PathVariable("rno")Long rno){
        Map<String,Long> map=new HashMap<>();
        replyService.deleteReply(rno);
        map.put("rno",rno);
        return map;
    }
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> Modify(@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO){
        replyDTO.setRno(rno);
        replyService.modifyReply(replyDTO);
        Map<String,Long> map=new HashMap<>();
        map.put("rno",rno);
        return map;
    }
}

