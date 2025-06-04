# 🏋️‍♀️ OneFit\_SSAFIT

**SSAFY 1학기 관통 프로젝트**
원하는 날짜와 시간에 운동기구 또는 PT를 예약할 수 있는 모바일 웹 서비스

---

## 📋 Contents

1. [📆 개발 기간](#개발-기간)
2. [👩‍💻 팀원-및-담당-파트](#팀원-및-담당-파트)
3. [🧩 주요-기능](#주요-기능)
4. [💻 서비스-화면](#서비스-화면)
5. [▶️ 실행-방법](#실행-방법)
6. [🛠️ 기술-스택-및-협업-툴](#기술-스택-및-협업-툴)
7. [📐 시스템-아키텍처](#시스템-아키텍처)
8. [🗄️ ERD](#erd)
9. [📅 일정](#일정)
10. [📡 API-명세서](#api-명세서)
11. [🪰 협업-툴](#협업-툴)

---

## 📆 개발 기간

* **2025.04.22 \~ 2025.05.27 (36일)**

---

## 👩‍💻 팀원 및 담당 파트

### 석예은 (팀장/BE & FE)

* Spring Security 및 JWT 기반 로그인/회원가입 구현
* AWS EC2 + RDS를 활용한 백업 서버 및 DB 배포
* S3를 활용한 프로필 사진 업로드 및 프로필 수정 구현
* 헬스장, 트레이너, 운동기구 검색 기능 구현
* 운동기구 예약 전체 구현
* PT/운동기구 확인과 통합구조 "내 예약" 구현

---

## 🧩 주요 기능

### 🔐 회원가입 / 로그인 / 탈퇴

* 사용자는 아이디가 번호를 입력하여 계정을 생성할 수 있습니다.
* 로그인을 통해 서비스에 접근할 수 있습니다.
* 자신의 계정을 삭제하고 회원 탈퇴도 가능합니다.

### ⚙️ 회원 정보 수정

* 전화 번호 및 이메일, 이름을 수정할 수 있습니다.
* 헬스장 및 트레이너 정보를 업데이트할 수 있습니다.
* 프로필 사진 업로드 기능 제공

### 👤 PT 예약

* 회원은 지정된 트레이너에게 PT 예약을 할 수 있습니다.

### 🏋️️ 운동기구 검색 & 필터

* 사용자는 사진 및 설명을 통해 기구 정보를 확인할 수 있습니다.
* 제품명, 운동 부위 기준의 검색 기능 제공
* 카테고리 기능을 통해 운동기구 필터링 가능

### ⏱️ 운동기구 예약 & 확인

* 기구 별 시간대 확인 가능
* 특정 시간에 특정 기구 예약 가능
* 자신이 예약한 기구 예약 목록 확인 가능

### 📜 게시판

* 사용자는 기존 게시물을 조회하고 키워드 기능으로 검색가능
* 게시물 작성, 조회, 수정, 삭제 가능
* 게시물 좋아요 및 댓글 기능

---

## 💻 서비스 화면

#### 회원가입 및 로그인 페이지

<table>
  <tr>
    <td><img src="docs/signup1.png" width="300"/></td>
    <td><img src="docs/signup2.png" width="300"/></td>
    <td><img src="docs/signup3.png" width="300"/></td>
    <td><img src="docs/signup4.png" width="300"/></td>
    <td><img src="docs/login.png" width="300"/></td>
  </tr>
</table>

▲ 회원가입 페이지: 1단계-개인정보 입력, 2단계-헬스장 선택, 3단계-트레이너 선택

#### 메인 페이지
<img width="385" alt="스크린샷 2025-06-04 오후 8 02 15" src="https://github.com/user-attachments/assets/c5b8c128-49ce-4dbc-b964-93139cc5c472" />
<br>
▲ 메인페이지: 게시판을 인기순과 최신순으로 조회

#### 게시판 

<table><tr>
<td><img src="https://github.com/user-attachments/assets/6531b412-0c08-4843-9ae5-98e9336493f2" width="300"/></td>
<td><img src="https://github.com/user-attachments/assets/4fcd7503-41a9-49e8-ab51-216a6ad50c42" width="300"/></td>
<td><img src="https://github.com/user-attachments/assets/8b121dd4-40ae-4206-9ded-7f2c6fae5af4" width="300"/></td>
<td><img src="https://github.com/user-attachments/assets/3ac4fe24-ab94-41fe-9837-99063d61ce0b" width="300"/></td>
</tr></table>
▲ 게시판 상세조회, 검색, 내 글 검색, 게시글 작성 페이지

#### 운동기구 예약 페이지
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/f88bc542-fbb5-4ebc-97fc-41562a54d507" width="300"/></td>
    <td><img src="https://github.com/user-attachments/assets/dafad5ec-b6a2-4f93-8230-6ccf875aae9e" width="300"/></td>
    <td><img src="https://github.com/user-attachments/assets/7a957201-28da-4070-9717-9f6d5c3065df" width="300"/></td>
  </tr>
</table>
▲ 운동기구 예약 페이지: 1단계-날짜 선택, 2단계-운동기구 선택, 3단계-시간 선택

#### PT 예약 페이지
<img width="381" alt="스크린샷 2025-06-04 오후 8 09 17" src="https://github.com/user-attachments/assets/dbb1d8f6-5733-47e1-b4dc-8704a3ffd1a6" />
<br>
▲ PT 예약 페이지: 트레이너는 회원정보에서 가져옴. 날짜와 시간만 선택

#### 나의 예약 페이지
<img width="384" alt="스크린샷 2025-06-04 오후 8 10 26" src="https://github.com/user-attachments/assets/0c7c9fea-eb3b-4d1c-b828-1b1e1a8bcafb" />
<br>
▲ 나의 예약 페이지: 날짜별 운동기구 예약과 PT 예약 목록을 통합 조회. 예약 취소 가능

#### 프로필
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/d1642527-9383-4254-a99c-8ab2870c8f4d" width="300"/></td>
    <td><img src="https://github.com/user-attachments/assets/56436160-bce3-4947-a11f-3cfd0a3b9350" width="300"/></td>
    <td><img src="https://github.com/user-attachments/assets/211864db-aab7-4aa7-a552-b4f1b68e5c89" width="300"/></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/48ccfd5b-cc4e-4004-8c25-b8dd0349414f" width="300"/></td>
    <td><img src="https://github.com/user-attachments/assets/70e4d86a-2d67-47e9-aa82-de24b5bc2972" width="300"/></td>
    <td><img src="https://github.com/user-attachments/assets/c4b9d796-297f-4731-9fed-af55d8dfccbb" width="300"/></td>
  </tr>
</table>
▲ 프로필 페이지: 프로필 조회, 프로필 수정(프로필 사진, 이름, 이메일, 헬스장, 트레이너), 비밀번호 변경 페이지

---

## 🛠️ 기술 스택 및 협업 도구

### 💻 Backend
![Java](https://img.shields.io/badge/Java-007396?style=flat&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=flat&logo=spring&logoColor=white)
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=flat&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Security-6DB33F?style=flat&logo=springsecurity&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=swagger&logoColor=black)
![MyBatis](https://img.shields.io/badge/MyBatis-005B9F?style=flat&logo=databricks&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat&logo=apachemaven&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=flat&logo=jsonwebtokens&logoColor=white)

### 🌐 Frontend
![Vue.js](https://img.shields.io/badge/Vue.js-4FC08D?style=flat&logo=vuedotjs&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=flat&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=javascript&logoColor=black)
![Bootstrap](https://img.shields.io/badge/Bootstrap-7952B3?style=flat&logo=bootstrap&logoColor=white)
![Axios](https://img.shields.io/badge/Axios-5A29E4?style=flat&logo=axios&logoColor=white)

### ☁️ Infrastructure & DevOps
![Amazon EC2](https://img.shields.io/badge/AWS_EC2-FF9900?style=flat&logo=amazonaws&logoColor=white)
![Amazon RDS](https://img.shields.io/badge/AWS_RDS-527FFF?style=flat&logo=amazonaws&logoColor=white)
![Amazon S3](https://img.shields.io/badge/AWS_S3-569A31?style=flat&logo=amazons3&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=flat&logo=git&logoColor=white)

### 🛠 협업 도구
![Figma](https://img.shields.io/badge/Figma-F24E1E?style=flat&logo=figma&logoColor=white)
![FigJam](https://img.shields.io/badge/FigJam-8E64D8?style=flat&logo=figjam&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-000000?style=flat&logo=notion&logoColor=white)


