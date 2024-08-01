package com.reservation.controller;

import com.reservation.entity.user.User;
import com.reservation.jwt.dto.TokenDto;
import com.reservation.service.BoardService;
import com.reservation.service.LikeService;
import com.reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {

  private final LikeService likeService;
  private final BoardService boardService;
  private final UserService userService;

  @GetMapping("/add/{boardId}")
  public String changeLike(@PathVariable Long boardId, Authentication auth) {
    User user = userService.getUser(auth.getName());
    likeService.changeLike(user.getId());
    return "redirect:/boards/" + boardService.getCategory(boardId) + "/" + boardId;
  }
//
//  @GetMapping("/delete/{boardId}")
//  public String deleteLike(@PathVariable Long boardId, Authentication auth) {
//    likeService.deleteLike(auth.getName(), boardId);
//    return "redirect:/boards/" + boardService.getCategory(boardId) + "/" + boardId;
//  }
}
