package com.reservation.controller;

import com.reservation.dto.board.CommentCreateRequest;
import com.reservation.entity.user.User;
import com.reservation.jwt.dto.TokenDto;
import com.reservation.service.BoardService;
import com.reservation.service.CommentService;
import com.reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;
  private final BoardService boardService;
  private final UserService userService;

  @PostMapping("/{boardId}")
  public String addComments(@PathVariable Long boardId, @ModelAttribute CommentCreateRequest req,
      Authentication auth, Model model) {
    User user = userService.getUser(auth.getName());

    commentService.writeComment(boardId, req, user.getId());

    model.addAttribute("message", "댓글이 추가되었습니다.");
    model.addAttribute("nextUrl", "/boards/" + boardService.getCategory(boardId) + "/" + boardId);
    return "printMessage";
  }

  @PostMapping("/{commentId}/edit")
  public String editComment(@PathVariable Long commentId, @ModelAttribute CommentCreateRequest req,
      Authentication auth, Model model) {
    User user = userService.getUser(auth.getName());
    Long boardId = commentService.editComment(commentId, req.getBody(),user.getId());
    model.addAttribute("message", boardId == null? "잘못된 요청입니다." : "댓글이 수정 되었습니다.");
    model.addAttribute("nextUrl", "/boards/" + boardService.getCategory(boardId) + "/" + boardId);
    return "printMessage";
  }

  @GetMapping("/{commentId}/delete")
  public String deleteComment(@PathVariable Long commentId,  Authentication auth, Model model) {
    User user = userService.getUser(auth.getName());
    Long boardId = commentService.deleteComment(commentId, user.getId());
    model.addAttribute("message", boardId == null? "작성자만 삭제 가능합니다." : "댓글이 삭제 되었습니다.");
    model.addAttribute("nextUrl", "/boards/" + boardService.getCategory(boardId) + "/" + boardId);
    return "printMessage";
  }
}
