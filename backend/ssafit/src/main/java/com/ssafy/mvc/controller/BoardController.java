package com.ssafy.mvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.mvc.model.dto.Board;
import com.ssafy.mvc.model.dto.BoardSearchCondition;
import com.ssafy.mvc.model.dto.User;
import com.ssafy.mvc.model.service.BoardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/boards")
@CrossOrigin
@Tag(name = "Board RESTful API", description = "게시판 CRUD 및 검색")
public class BoardController {

	// 서비스 의존성 주입
	@Autowired
    private BoardService boardService;
	
	// 게시글 목록 조회 (최신순)
	@Operation(summary = "최신순 게시글 목록 조회", description = "게시글을 최신순(created_at DESC)으로 조회합니다.")
	@GetMapping("/latest")
    public ResponseEntity<List<Board>> getAllLatest(@ModelAttribute BoardSearchCondition condition) {
        List<Board> list = boardService.getAllLatest(condition);
        return ResponseEntity.ok(list);  // 항상 리스트 반환
    }
	
	// 게시글 목록 조회 (인기순)
	@Operation(summary = "인기순 게시글 목록 조회", description = "게시글을 인기순(like_count DESC)으로 조회합니다.")
	@GetMapping("/popular")
    public ResponseEntity<List<Board>> getAllPopular(@ModelAttribute BoardSearchCondition condition) {
        return ResponseEntity.ok(boardService.getAllPopular(condition));
    }
	
	// 내 게시글 목록 조회
	@Operation(summary = "내가 작성한 게시글 목록 조회", description = "특정 사용자가 작성한 게시글 목록을 조회합니다.")
	@GetMapping("/my/{userId}")
    public ResponseEntity<List<Board>> getMyBoards(@PathVariable int userId,
                                                   @ModelAttribute BoardSearchCondition condition) {
        return ResponseEntity.ok(boardService.getMyBoards(userId, condition));
    }
	
	// 게시글 상세 조회
	@Operation(summary = "게시글 상세 조회", description = "게시글 ID를 기반으로 해당 게시글을 조회합니다. 조회수도 함께 증가합니다.")
	@GetMapping("/{boardId}")
    public ResponseEntity<Board> getBoardDetail(@PathVariable int boardId) {
        Board board = boardService.getBoardDetail(boardId);
        if (board != null) {
            return ResponseEntity.ok(board);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
	
	// 게시글 검색 (제목, 내용, 작성자)
	@Operation(summary = "게시글 검색", description = "제목, 내용, 작성자 기준으로 게시글을 검색합니다.")
	@GetMapping("/search")
    public ResponseEntity<List<Board>> search(@ModelAttribute BoardSearchCondition condition) {
        return ResponseEntity.ok(boardService.search(condition));
    }
	
	// 게시글 작성 (사진 업로드도 같이)
	@Operation(
		    summary = "게시글 작성",
		    description = "게시글을 등록합니다. 이미지 파일도 함께 업로드할 수 있습니다.",
		    security = @SecurityRequirement(name = "jwtAuth")
		)
		@PostMapping(consumes = "multipart/form-data")
		public ResponseEntity<String> write(
		        @RequestPart("board") Board board,
		        @RequestPart(value = "files", required = false) List<MultipartFile> imageFiles,
		        @AuthenticationPrincipal User user  //  로그인한 유저 정보 추출
		) {
		    board.setUserId(user.getUserId());      //  user_id 직접 설정
		    boardService.write(board, imageFiles);
		    return ResponseEntity.status(HttpStatus.CREATED).body("게시글이 등록되었습니다.");
		}
	
	// 게시글 수정	
	@PutMapping(value = "/{boardId}", consumes = "multipart/form-data")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> update(@PathVariable int boardId,
	                                     @RequestPart("board") Board board,  // JSON으로 전달됨
	                                     @RequestPart(value = "files", required = false) List<MultipartFile> imageFiles,
	                                     @RequestParam(value = "deleteImageUrls", required = false) List<String> deleteImageUrls) {

	    User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    int currentUserId = loginUser.getUserId();

	    Board origin = boardService.getBoardDetail(boardId);
	    if (origin == null || origin.getUserId() != currentUserId) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
	    }

	    board.setBoardId(boardId);
	    board.setDeleteImageUrls(deleteImageUrls);  // null 허용됨
	    boardService.update(board, imageFiles);     // imageFiles도 null 허용됨

	    return ResponseEntity.ok("게시글이 수정되었습니다.");
	}


	
	// 게시글 삭제
	@DeleteMapping("/{boardId}")
	@PreAuthorize("hasRole('USER')")
	@Operation(summary = "게시글 삭제", description = "게시글 ID를 기반으로 해당 게시글을 삭제합니다. 작성자 본인만 삭제할 수 있습니다.")
	public ResponseEntity<String> delete(@PathVariable int boardId) {
	    //  principal을 User 객체로 형변환
	    User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    int currentUserId = loginUser.getUserId(); // userId만 추출
	
	    Board board = boardService.getBoardDetail(boardId);
	    if (board == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글을 찾을 수 없습니다.");
	    }

	    if (board.getUserId() != currentUserId) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인만 삭제할 수 있습니다.");
	    }
	
	    boardService.delete(boardId);
	    return ResponseEntity.ok("게시글이 삭제되었습니다.");
	}


	
	// 게시글 좋아요 등록 / 취소
	@Operation(
		    summary = "좋아요 등록/취소 (토글)",
		    description = "해당 사용자가 이미 좋아요를 눌렀으면 취소하고, 누르지 않았다면 좋아요를 등록합니다."
		)
	@PatchMapping("/{boardId}/like")
	public ResponseEntity<String> toggleLike(@PathVariable int boardId, @RequestParam int userId) {
	    boolean isLiked = boardService.isLiked(boardId, userId);

	    if (isLiked) {  // 이미 좋아요 누른 게시글이라면 취소
	        boardService.unlike(boardId, userId);
	        return ResponseEntity.ok("좋아요가 취소되었습니다.");
	    } else {  // 좋아요 누르지 않은 게시글이라면 등록
	        boardService.like(boardId, userId);
	        return ResponseEntity.ok("좋아요가 등록되었습니다.");
	    }
	}
	
	// 게시글 좋아요 여부 확인
	@Operation(summary = "좋아요 여부 확인", description = "특정 사용자가 해당 게시글에 좋아요를 눌렀는지 여부를 확인합니다.")
	@GetMapping("/{boardId}/like")
    public ResponseEntity<Boolean> isLiked(@PathVariable int boardId, @RequestParam int userId) {
        boolean result = boardService.isLiked(boardId, userId);
        return ResponseEntity.ok(result);
    }
	
	// 댓글 수 조회
	@Operation(summary = "댓글 수 조회", description = "해당 게시글에 달린 댓글 개수를 반환합니다.")
	@GetMapping("/{boardId}/comment-count")
    public ResponseEntity<Integer> getCommentCount(@PathVariable int boardId) {
        int count = boardService.countComments(boardId);
        return ResponseEntity.ok(count);
    }
	
}
