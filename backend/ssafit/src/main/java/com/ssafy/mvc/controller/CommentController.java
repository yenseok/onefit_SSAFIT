package com.ssafy.mvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mvc.model.dto.Comment;
import com.ssafy.mvc.model.dto.User;
import com.ssafy.mvc.model.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/comments")
@CrossOrigin
@Tag(name = "Comment RESTful API", description = "댓글 CRUD 및 검색")
public class CommentController {

	@Autowired
    private CommentService commentService;

	// 댓글 작성
	@PostMapping
	@PreAuthorize("hasRole('USER')")
	@Operation(summary = "댓글 등록", description = "새로운 댓글을 작성합니다. 로그인한 사용자만 작성 가능합니다.")
	public ResponseEntity<String> insert(@RequestBody Comment comment) {
	    User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    int currentUserId = currentUser.getUserId();
	    comment.setUserId(currentUserId);

	    commentService.insert(comment);
	    return ResponseEntity.ok("댓글이 등록되었습니다.");
	}

	@PutMapping("/{commentId}")
	@PreAuthorize("hasRole('USER')")
	@Operation(summary = "댓글 수정", description = "기존 댓글의 내용을 수정합니다. 작성자 본인만 수정할 수 있습니다.")
	public ResponseEntity<String> update(@PathVariable int commentId, @RequestBody Comment comment) {
	    User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    int currentUserId = currentUser.getUserId();

	    Comment existing = commentService.getById(commentId);
	    if (existing == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("댓글을 찾을 수 없습니다.");
	    }

	    if (existing.getUserId() != currentUserId) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인만 댓글을 수정할 수 있습니다.");
	    }

	    comment.setCommentId(commentId);
	    comment.setUserId(currentUserId);
	    commentService.update(comment);
	    return ResponseEntity.ok("댓글이 수정되었습니다.");
	}

	@DeleteMapping("/{commentId}")
	@PreAuthorize("hasRole('USER')")
	@Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다. 작성자 본인만 삭제할 수 있습니다.")
	public ResponseEntity<String> delete(@PathVariable int commentId) {
	    User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    int currentUserId = currentUser.getUserId();

	    Comment comment = commentService.getById(commentId);
	    if (comment == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("댓글을 찾을 수 없습니다.");
	    }

	    if (comment.getUserId() != currentUserId) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인만 삭제할 수 있습니다.");
	    }

	    commentService.delete(commentId);
	    return ResponseEntity.ok("댓글이 삭제되었습니다.");
	}

 
    // 댓글 조회
    @Operation(summary = "게시글의 댓글 목록 조회", description = "특정 게시글에 달린 전체 댓글 목록을 반환합니다.")
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<Comment>> getByBoard(@PathVariable int boardId) {
        return ResponseEntity.ok(commentService.getCommentsByBoardId(boardId));
    }
}