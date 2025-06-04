package com.ssafy.mvc.model.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.mvc.model.dao.BoardDao;
import com.ssafy.mvc.model.dao.BoardImageDao;
import com.ssafy.mvc.model.dao.BoardLikeDao;
import com.ssafy.mvc.model.dao.CommentDao;
import com.ssafy.mvc.model.dto.Board;
import com.ssafy.mvc.model.dto.BoardSearchCondition;
import com.ssafy.mvc.model.dto.Comment;
import com.ssafy.mvc.util.FileUploadUtil;

@Service
public class BoardServiceImpl implements BoardService{

	// 의존성 주입
	@Autowired
    private BoardDao boardDao;

    @Autowired
    private BoardLikeDao boardLikeDao;

    @Autowired
    private CommentDao commentDao;
    
    @Autowired
    private BoardImageDao boardImageDao;

    @Autowired
    private FileUploadUtil fileUploadUtil;
    

    // 게시글 목록 조회 (최신순)
    @Override
    public List<Board> getAllLatest(BoardSearchCondition condition) {
        condition.setSortType("latest");  // created_at DESC
        return boardDao.selectAll(condition);
    }

    // 게시글 목록 조회 (인기순)
    @Override
    public List<Board> getAllPopular(BoardSearchCondition condition) {
        condition.setSortType("popular");  // like_count DESC
        return boardDao.selectAll(condition);
    }

    // 내 게시글 목록 조회
    @Override
    public List<Board> getMyBoards(int userId, BoardSearchCondition condition) {
        return boardDao.selectByUserId(userId, condition);
    }

    // 게시글 검색 (제목, 내용, 작성자)
    @Override
    public List<Board> search(BoardSearchCondition condition) {
        return boardDao.search(condition); // 검색 조건 포함 쿼리
    }

    // 게시글 상세 조회   
    @Override
    public Board getBoardDetail(int boardId) {
        boardDao.increaseViewCount(boardId);  // 조회수 증가
        Board board = boardDao.select(boardId); // 게시글 정보 조회

        if (board == null) {
            return null;  //  null인 경우 바로 반환
        }

        // 댓글 리스트도 함께 세팅
        List<Comment> comments = commentDao.selectByBoardId(boardId);
        board.setComments(comments);

        // 이미지 목록 조회 추가
        List<String> imageUrls = boardImageDao.selectByBoardId(boardId);
        board.setImageUrls(imageUrls);

        return board;
    }


    // 게시글 작성
    @Override
    public void write(Board board, List<MultipartFile> imageFiles) {
    	try {
            board.setViewCount(0);
            board.setLikeCount(0);
            board.setCommentCount(0);

            // 게시글 먼저 저장 (auto_increment 키 생성)
            boardDao.insert(board);

            // 이미지 저장 처리
            if (imageFiles != null && !imageFiles.isEmpty()) {
                for (int i = 0; i < imageFiles.size(); i++) {
                    MultipartFile file = imageFiles.get(i);
                    
                    if (!file.isEmpty()) {
                        String savedPath = fileUploadUtil.saveFile(file);
//                        System.out.println("저장된 경로: " + savedPath);  // 추가
                        boolean isThumbnail = (i == 0);
                        boardImageDao.insert(board.getBoardId(), savedPath, isThumbnail, i);

                        if (isThumbnail) {
                            board.setThumbnail(savedPath);
                            boardDao.update(board); // 썸네일 포함된 board update
                        }
                    }
                }
            } else {
                board.setThumbnail(null);
                boardDao.update(board);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일 저장 중 오류 발생");
        }
    }
    
    

    // 게시글 수정
    @Override
    public void update(Board board, List<MultipartFile> imageFiles) {
        try {
            int boardId = board.getBoardId();

            // 1. 삭제 요청한 이미지 제거
            List<String> deleteUrls = board.getDeleteImageUrls();
            if (deleteUrls != null && !deleteUrls.isEmpty()) {
                for (String url : deleteUrls) {
                    boardImageDao.deleteByBoardIdAndImageUrl(boardId, url);
                }
            }

            // 2. 기존 이미지 목록 재조회
            List<String> existingImages = boardImageDao.selectByBoardId(boardId);

            // 3. 이미지 순서 정리 및 썸네일 결정
            String thumbnailPath = existingImages.isEmpty() ? null : existingImages.get(0);  // <- null 처리

            for (int i = 0; i < existingImages.size(); i++) {
                String url = existingImages.get(i);
                boolean isThumbnail = (i == 0);
                boardImageDao.updateImageOrder(boardId, url, i);
                boardImageDao.updateThumbnailFlag(boardId, url, isThumbnail);
            }

            // 4. 새 이미지 저장
            if (imageFiles != null && !imageFiles.isEmpty()) {
                int startIndex = existingImages.size();
                for (int i = 0; i < imageFiles.size(); i++) {
                    MultipartFile file = imageFiles.get(i);
                    if (!file.isEmpty()) {
                        String savedPath = fileUploadUtil.saveFile(file);
                        boolean isThumbnail = (startIndex + i == 0);
                        boardImageDao.insert(boardId, savedPath, isThumbnail, startIndex + i);
                        if (isThumbnail) {
                            thumbnailPath = savedPath;
                        }
                    }
                }
            }

            // 5. 제목/내용/썸네일 갱신
            Board origin = boardDao.select(boardId);
            if (board.getTitle() == null) board.setTitle(origin.getTitle());
            if (board.getContent() == null) board.setContent(origin.getContent());

            board.setThumbnail(thumbnailPath);  // <- null도 허용
            boardDao.update(board);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일 저장 또는 이미지 처리 중 오류 발생");
        }
    }

    // 게시글 삭제
    @Override
    public void delete(int boardId) {
        boardDao.delete(boardId);
    }

    // 게시글 좋아요 선택
    @Override
    public void like(int boardId, int userId) {
        boardLikeDao.insert(boardId, userId);
        boardDao.increaseLikeCount(boardId);
    }

    // 게시글 좋아요 취소
    @Override
    public void unlike(int boardId, int userId) {
        boardLikeDao.delete(boardId, userId);
        boardDao.decreaseLikeCount(boardId);
    }

    // 게시글 좋아요 여부 확인
    @Override
    public boolean isLiked(int boardId, int userId) {
        return boardLikeDao.isLiked(boardId, userId) > 0;
    }

    // 조회수 증가
    @Override
    public void increaseViewCount(int boardId) {
        boardDao.increaseViewCount(boardId);
    }

    // 댓글 수 조회
    @Override
    public int countComments(int boardId) {
        return commentDao.countByBoardId(boardId); // 실시간 COUNT
    }

}
