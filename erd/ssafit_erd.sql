-- 기존 데이터베이스 삭제 후 생성
DROP DATABASE IF EXISTS ssafit;
CREATE DATABASE ssafit DEFAULT CHARACTER SET utf8mb4;
USE ssafit;

-- 헬스장 테이블
CREATE TABLE gym (
    gym_id INT AUTO_INCREMENT PRIMARY KEY,  -- 헬스장 id
    gym_name VARCHAR(50) NOT NULL,  -- 헬스장 명
    gym_address VARCHAR(100) NOT NULL  -- 헬스장 주소
);

-- 트레이너 테이블
CREATE TABLE trainer (
    trainer_id INT AUTO_INCREMENT PRIMARY KEY,  -- 트레이너 id
    trainer_name VARCHAR(50) NOT NULL,  -- 트레이너 이름
    gym_id INT NOT NULL,  -- 해당 트레이너가 속한 헬스장 id
    trainer_img VARCHAR(500), 
    FOREIGN KEY (gym_id) REFERENCES gym(gym_id) ON DELETE CASCADE
);

-- 회원정보 테이블
CREATE TABLE user (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    nickname VARCHAR(50) UNIQUE,
    email VARCHAR(100),
    password VARCHAR(255),
    phone VARCHAR(20),
    gym_id INT NOT NULL,  -- 해당 회원이 속한 헬스장 id
    trainer_id INT,  -- 해당 회원이 pt받는 트레이너 id
    profile_image VARCHAR(500),
    role VARCHAR(20)  DEFAULT 'USER',
    FOREIGN KEY (gym_id) REFERENCES gym(gym_id) ON DELETE CASCADE,
    FOREIGN KEY (trainer_id) REFERENCES trainer(trainer_id) ON DELETE CASCADE
);

-- 게시글
CREATE TABLE board (
    board_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    content TEXT,
    thumbnail VARCHAR(500) DEFAULT null,  -- 썸네일 이미지
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    user_id INT,
    view_count INT DEFAULT 0,  -- 조회수
    like_count INT DEFAULT 0,  -- 좋아요 수
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- 게시글 이미지
CREATE TABLE board_image (
    image_id INT AUTO_INCREMENT PRIMARY KEY,
    board_id INT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    is_thumbnail BOOLEAN DEFAULT FALSE,  -- 대표 이미지 여부
    image_order INT DEFAULT 0,  -- 한 게시글 내에 있는 이미지들 중 순서
    FOREIGN KEY (board_id) REFERENCES board(board_id) ON DELETE CASCADE,
    UNIQUE (board_id, image_url)
);

-- 게시글 좋아요 
CREATE TABLE board_like (
    board_id INT,
    user_id INT,
    PRIMARY KEY (board_id, user_id),
    FOREIGN KEY (board_id) REFERENCES board(board_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- 댓글
CREATE TABLE comment (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    content TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    user_id INT,
    board_id INT,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (board_id) REFERENCES board(board_id) ON DELETE CASCADE
);

-- PT 예약 테이블
CREATE TABLE pt (
    pt_id INT AUTO_INCREMENT PRIMARY KEY,  -- PT 예약 id
    user_id INT NOT NULL,  -- 회원 id
    trainer_id INT NOT NULL,  -- 트레이너 id
    pt_date DATE NOT NULL,  -- 예약 날짜
    pt_time TIME NOT NULL,  -- 예약 시간
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (trainer_id) REFERENCES trainer(trainer_id) ON DELETE CASCADE
);

-- 운동기구 테이블
CREATE TABLE equipment (
    equipment_id INT AUTO_INCREMENT PRIMARY KEY,  -- 운동기구 id
    gym_id INT NOT NULL,  -- 해당 운동기구가 속한 헬스장 id
    equipment_name VARCHAR(50) NOT NULL,  -- 운동기구 명
    equipment_img  VARCHAR(255),  -- 운동기구 아이콘 이미지 url
    FOREIGN KEY (gym_id) REFERENCES gym(gym_id) ON DELETE CASCADE
);

-- 태그 테이블
CREATE TABLE tag (
    tag_id INT AUTO_INCREMENT PRIMARY KEY,  -- 태그 id
    tag_name VARCHAR(30) NOT NULL UNIQUE  -- 태그 명
);

-- 운동기구 - 태그 연결 테이블 (중간 다리 역할)
CREATE TABLE equipment_tag (
  equipment_id INT NOT NULL,
  tag_id INT NOT NULL,
  PRIMARY KEY (equipment_id, tag_id),
  FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id) ON DELETE CASCADE,
  FOREIGN KEY (tag_id) REFERENCES tag(tag_id) ON DELETE CASCADE
);

-- 운동기구 예약 테이블
CREATE TABLE equipment_reservation (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,  -- 운동기구 예약 id
    user_id INT NOT NULL,  -- 회원 id
    equipment_id INT NOT NULL,  -- 운동기구 id
    reservation_date DATE NOT NULL,  -- 예약 날짜
    reservation_time TIME NOT NULL,  -- 예약 시간
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id) ON DELETE CASCADE
);

-- 헬스장 더미데이터
INSERT INTO gym (gym_name, gym_address) VALUES ('스포애니 역삼역점', '서울 강남구 테헤란로 146');
INSERT INTO gym (gym_name, gym_address) VALUES ('버핏그라운드 역삼', '서울 강남구 테헤란로 142 아크플레이스 B1층');
INSERT INTO gym (gym_name, gym_address) VALUES ('제임스짐휘트니스 역삼점', '강남구 테헤란로 217 오렌지플레닛 B1층');

-- 트레이너 더미데이터
INSERT INTO trainer (trainer_name, gym_id) VALUES ('김시영', 1);
INSERT INTO trainer (trainer_name, gym_id) VALUES ('양명균', 1);
INSERT INTO trainer (trainer_name, gym_id) VALUES ('이슬', 1);

INSERT INTO trainer (trainer_name, gym_id) VALUES ('추수연', 2);
INSERT INTO trainer (trainer_name, gym_id) VALUES ('장수연', 2);
INSERT INTO trainer (trainer_name, gym_id) VALUES ('석예은', 2);

INSERT INTO trainer (trainer_name, gym_id) VALUES ('카리나', 3);
INSERT INTO trainer (trainer_name, gym_id) VALUES ('장원영', 3);
INSERT INTO trainer (trainer_name, gym_id) VALUES ('설윤', 3);

-- 회원 더미데이터
INSERT INTO user (name, nickname, email, password, phone, gym_id, trainer_id)
VALUES ('홍길동', 'hong123', 'hong@test.com', '1234', '01012345678', 1, 1);
INSERT INTO user (name, nickname, email, password, phone, gym_id, trainer_id)
VALUES ('김영희', 'kimyh', 'kim@test.com', '1234', '01023456789', 2, 2);
INSERT INTO user (name, nickname, email, password, phone, gym_id, trainer_id)
VALUES ('이철수', 'leech', 'lee@test.com', '1234', '01034567890', 3, 3);
INSERT INTO user (name, nickname, email, password, phone, gym_id, trainer_id)
VALUES ('아무개', '밥도둑', 'aa@test.com', '1234', '01034567890', 3, 2);

-- 게시판 더미데이터
INSERT INTO board (title, content, user_id, thumbnail)
VALUES ('한강 러닝 하실 분', '모집합니다.', 2,'/upload/board/han.jpg');
INSERT INTO board (title, content, user_id)
VALUES ('스포애니 김시영 트레이너님 너무 좋아요..!!', '필요한 부분을 딱딱 알려주십니다.', 3);
INSERT INTO board (title, content, user_id, thumbnail)
VALUES ('나만의 건강식', '맛있고 건강해요!', 4, '/upload/board/1748315455478_foods.jpg');
INSERT INTO board (title, content, user_id, thumbnail)
VALUES ('나만의 운동 루틴 소개', '정말 쉽죠~?', 1, '/upload/board/new.png');
INSERT INTO board (title, content, user_id)
VALUES ('등근육 키울 수 있는 운동 추천 부탁', '드립니다', 2);


-- 게시판 이미지 더미데이터
INSERT INTO board_image (board_id, image_url, is_thumbnail)
VALUES (3, '/upload/board/1748315455478_foods.jpg', 1);
INSERT INTO board_image (board_id, image_url, is_thumbnail)
VALUES (1, '/upload/board/han.jpg', 1);
INSERT INTO board_image (board_id, image_url, is_thumbnail)
VALUES (1, '/upload/board/han2.jpg', 0);
INSERT INTO board_image (board_id, image_url, is_thumbnail)
VALUES (4, '/upload/board/new.png', 1);

-- 게시판 좋아요 더미데이터
INSERT INTO board_like (board_id, user_id) VALUES (1, 2);
INSERT INTO board_like (board_id, user_id) VALUES (1, 3);
INSERT INTO board_like (board_id, user_id) VALUES (2, 4);
INSERT INTO board_like (board_id, user_id) VALUES (3, 1);
INSERT INTO board_like (board_id, user_id) VALUES (4, 3);

-- 댓글 더미데이터
INSERT INTO comment (content, user_id, board_id)
VALUES ('동의합니다', 1, 2);
INSERT INTO comment (content, user_id, board_id)
VALUES ('와 꼭 따라해 봐야겠어요~', 2, 4);
INSERT INTO comment (content, user_id, board_id)
VALUES ('클라이밍 추천함', 3, 5);

-- PT 예약 더미데이터
INSERT INTO pt (user_id, trainer_id, pt_date, pt_time)
VALUES (1, 1, CURDATE() + INTERVAL 1 DAY, '10:00:00');
INSERT INTO pt (user_id, trainer_id, pt_date, pt_time)
VALUES (2, 2, CURDATE() + INTERVAL 2 DAY, '10:00:00');
INSERT INTO pt (user_id, trainer_id, pt_date, pt_time)
VALUES (3, 3, CURDATE() + INTERVAL 3 DAY, '10:00:00');

-- 운동기구 더미데이터
INSERT INTO equipment (gym_id, equipment_name, equipment_img)
VALUES (1, '벤치 프레스', 'bench_press_gray.png');
INSERT INTO equipment (gym_id, equipment_name, equipment_img)
VALUES (1, '체스트 프레스', 'chest_press_gray.png');
INSERT INTO equipment (gym_id, equipment_name, equipment_img)
VALUES (1, '래풀다운', 'lat_pull_down_gray.png');
INSERT INTO equipment (gym_id, equipment_name, equipment_img)
VALUES (1, '런닝머신', 'running_machine_gray.png');
INSERT INTO equipment (gym_id, equipment_name, equipment_img)
VALUES (1, '레그 익스텐션', 'leg_extension_gray.png');

INSERT INTO equipment (gym_id, equipment_name, equipment_img)
VALUES (2, '벤치 프레스', 'bench_press_gray.png');
INSERT INTO equipment (gym_id, equipment_name, equipment_img)
VALUES (2, '체스트 프레스', 'chest_press_gray.png');
INSERT INTO equipment (gym_id, equipment_name, equipment_img)
VALUES (2, '래풀다운', 'lat_pull_down_gray.png');
INSERT INTO equipment (gym_id, equipment_name, equipment_img)
VALUES (2, '런닝머신', 'running_machine_gray.png');
INSERT INTO equipment (gym_id, equipment_name, equipment_img)
VALUES (2, '레그 익스텐션', 'leg_extension_gray.png');

INSERT INTO equipment (gym_id, equipment_name, equipment_img)
VALUES (3, '벤치 프레스', 'bench_press_gray.png');
INSERT INTO equipment (gym_id, equipment_name, equipment_img)
VALUES (3, '체스트 프레스', 'chest_press_gray.png');
INSERT INTO equipment (gym_id, equipment_name, equipment_img)
VALUES (3, '래풀다운', 'lat_pull_down_gray.png');
INSERT INTO equipment (gym_id, equipment_name, equipment_img)
VALUES (3, '런닝머신', 'running_machine_gray.png');
INSERT INTO equipment (gym_id, equipment_name, equipment_img)
VALUES (3, '레그 익스텐션', 'leg_extension_gray.png');

-- 운동기구 태그 더미데이터
INSERT INTO tag (tag_name) VALUES ('하체');
INSERT INTO tag (tag_name) VALUES ('가슴');
INSERT INTO tag (tag_name) VALUES ('등');
INSERT INTO tag (tag_name) VALUES ('승모근');
INSERT INTO tag (tag_name) VALUES ('어깨');
INSERT INTO tag (tag_name) VALUES ('팔');
INSERT INTO tag (tag_name) VALUES ('광배근');
INSERT INTO tag (tag_name) VALUES ('유산소');
INSERT INTO tag (tag_name) VALUES ('대퇴사두근');

-- 운동기구 태그 - 연결 더미데이터
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (1, 1);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (2, 2);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (3, 3);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (1, 4);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (2, 5);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (2, 6);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (3, 7);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (4, 8);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (4, 1);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (5, 1);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (5, 9);

INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (6, 1);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (7, 2);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (8, 3);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (6, 4);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (7, 5);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (7, 6);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (8, 7);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (9, 8);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (9, 1);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (10, 1);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (10, 9);

INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (11, 1);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (12, 2);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (13, 3);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (11, 4);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (12, 5);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (12, 6);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (13, 7);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (14, 8);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (14, 1);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (15, 1);
INSERT INTO equipment_tag (equipment_id, tag_id) VALUES (15, 9);

-- 운동기구 예약 더미데이터
INSERT INTO equipment_reservation (user_id, equipment_id, reservation_date, reservation_time)
VALUES (1, 1, CURDATE() + INTERVAL 1 DAY, '14:00:00');
INSERT INTO equipment_reservation (user_id, equipment_id, reservation_date, reservation_time)
VALUES (2, 2, CURDATE() + INTERVAL 2 DAY, '14:00:00');
INSERT INTO equipment_reservation (user_id, equipment_id, reservation_date, reservation_time)
VALUES (3, 3, CURDATE() + INTERVAL 3 DAY, '14:00:00');

-- select 문 데이터 확인용
-- 헬스장
SELECT * FROM gym;
-- 트레이너
SELECT * FROM trainer;
-- 회원
SELECT * FROM user; 
-- 게시글
SELECT * FROM board;
-- 게시글 이미지
SELECT * FROM board_image; 

-- 게시글 좋아요
SELECT * FROM board_like;
-- 댓글
SELECT * FROM comment;
-- PT 예약
SELECT * FROM pt;
-- 운동기구
SELECT * FROM equipment;
-- 태그
SELECT * FROM tag;
-- 운동기구-태그 연결
SELECT * FROM equipment_tag;
-- 운동기구 예약
SELECT * FROM equipment_reservation;

SELECT nickname, password FROM user WHERE nickname = 'hong123';




