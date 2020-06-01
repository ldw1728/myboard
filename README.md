# myboard
## 내용
spring boot 를 이용하여 구현한 게시판    
http://13.209.44.126:8080

## 기술
frontend - bootstap    
backend -    
* framwork : springboot(security, thymeleaf, jpa, lombok, modelmapper)   
        - mvc pattern 
* DB : mysql    
* server : aws ec2 amazon linux2    
## 기능
 - boot strap 을 이용하여 front end 구현
 - security로 권한 및 로그인 기능 구현.
 - mysql과 연동하여 CRUD기능 구현 (생성, 조회, 수정, 삭제)
 - 검색, 페이징, 댓글 기능, 조회수 기능
 - aws ec2를 이용하여 배포.
 
 ## issue 
 로컬에서는 문제없었던 인코딩문제가 서버에서 발생. 한글로 입력 시 오류.
 -> ec2서버 db의 각 table마다 utf8로 charater set을 설정하여 해결 
 mvn package시에 오류가 난다면 자바 버전확인, maven java 경로 확인, 프로젝트 폴더의 권한모드 확인.
 

## 구동
<img src="/myboard/login.PNG" width="400" height="250"></img>
<img src="/myboard/main.PNG" width="400" height="250"></img>
<img src="/myboard/new.PNG" width="400" height="250"></img>
<img src="/myboard/detail.PNG" width="400" height="250"></img>
<img src="/myboard/detailComment.PNG" width="400" height="250"></img>
