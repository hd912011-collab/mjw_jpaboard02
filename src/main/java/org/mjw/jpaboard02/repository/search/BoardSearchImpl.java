package org.mjw.jpaboard02.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.mjw.jpaboard02.domain.Board;
import org.mjw.jpaboard02.domain.QBoard;
import org.mjw.jpaboard02.domain.QReply;
import org.mjw.jpaboard02.dto.BoardListReplyCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {
        QBoard qboard = QBoard.board;
        JPQLQuery<Board> query=from(qboard);
        BooleanBuilder builder=new BooleanBuilder();
        builder.or(qboard.title.containsIgnoreCase("4")); //or title like '%1%'
        builder.or(qboard.content.containsIgnoreCase("4")); //or content like '%1%'
        builder.or(qboard.member.username.containsIgnoreCase("4"));
        query.where(builder); // where title like '%4%' or content like '%4%' or author like '%4%' and bno>0 limit 0,5
        query.where(qboard.bno.gt(0));
        this.getQuerydsl().applyPagination(pageable, query);
        List<Board> list=query.fetch();
        long count=query.fetchCount();
        return new PageImpl<Board>(list,pageable,count);
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard qboard = QBoard.board;
        JPQLQuery<Board> query=from(qboard);

        if(types!=null&&types.length>0 && keyword!=null){
         BooleanBuilder builder=new BooleanBuilder();
         for(String type:types){
             switch (type) {
                 case "t":
                     builder.or(qboard.title.containsIgnoreCase(keyword));
                     break;
                 case "c":
                     builder.or(qboard.content.containsIgnoreCase(keyword));
                     break;
                 case "w":
                     builder.or(qboard.member.username.containsIgnoreCase(keyword));
                }
            }
            query.where(builder);
        }
        query.where(qboard.bno.gt(0));
        this.getQuerydsl().applyPagination(pageable, query);
        List<Board> list=query.fetch();
        long count=query.fetchCount();
        return new PageImpl<>(list,pageable,count);
    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplycount(String[] types, String keyword, Pageable pageable) {
        QBoard qboard = QBoard.board;
        QReply qreply = QReply.reply;
        JPQLQuery<Board> query=from(qboard);
        query.leftJoin(qreply).on(qreply.board.eq(qboard));
        query.groupBy(qboard);

        if(types!=null && types.length>0 && keyword!=null){
            BooleanBuilder builder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t" :
                        builder.or(qboard.title.containsIgnoreCase(keyword));
                        break;
                    case "c" :
                        builder.or(qboard.content.containsIgnoreCase(keyword));
                        break;
                    case "w" :
                        builder.or(qboard.member.username.containsIgnoreCase(keyword));
                }
            }
            query.where(builder);
        }
        query.where(qboard.bno.gt(0));

        JPQLQuery<BoardListReplyCountDTO> dtoQuery=query.select(
                Projections.bean(BoardListReplyCountDTO.class,
                        qboard.bno,
                        qboard.title,
                        qboard.member.username.as("author"),
                        qboard.regDate,
                        qboard.readcount,
                        qreply.count().as("replyCount")));
        this.getQuerydsl().applyPagination(pageable,query);
        List<BoardListReplyCountDTO> dtoList=dtoQuery.fetch();
        long count=query.fetchCount();
        return new PageImpl<>(dtoList,pageable,count);
    }
}

