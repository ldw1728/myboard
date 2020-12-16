# myboard
## 내용
spring boot 를 이용하여 구현한 게시판    
~~http://3.34.150.212:8888/~~ (aws관련 문제로 잠시 접속불가)

## 기술
frontend - bootstap    
backend -    
* framwork : springboot(security, OAuth2, thymeleaf, jpa, lombok, modelmapper)   
        - mvc pattern 
* DB : mysql    
* server : aws ec2 amazon linux2 / aws RDS MySQL

## 구조
<img src="/myboard/myboardClass.PNG" width="1000" height="600"></img>

## 기능
 - boot strap 을 이용하여 front end 구현    
 - security로 권한 및 로그인 기능 구현.    
 - AWS RDS mysql과 연동하여 CRUD기능 구현 (생성, 조회, 수정, 삭제)    
 - 검색, 페이징, 댓글 기능, 조회수 기능    
 - ckeditor를 이용하여 에디터 기능 및 사진 업로드 기능(클립보드 첨부, 드래그앤드랍)    
 - aws ec2를 이용하여 배포.
 - OAuth2를 사용하여 소셜로그인 기능 추가.
 
 ## Issue 
 * AWS VPC를 생성, VPC내부의 private subnet(DBServer)와 public subnet(EC2 webserver)로 구성하여 
        DBServer를 외부에서는 접근하지못하고 ec2에서만 접근할 수 있도록함.
 * 로컬에서는 문제없었던 인코딩문제가 서버에서 발생. 한글로 입력 시 오류.    
 -> ec2서버 db의 각 table마다 utf8로 charater set을 설정하여 해결     
 * mvn package시에 오류가 난다면 자바 버전확인, maven java 경로 확인, 프로젝트 폴더의 권한모드 확인.   
 * ckeditor에디터의 사진업로드 기능은 adapter가 필요로 하는데 이미 개발되어진 adapter는    
 유료이기 때문에 CustomUploadAdapter를 구현하여 사용. 공식문서참조.
 에디터에 첨부 시 서버에 업로드, 사진 업로드된 경로를 이용하여 img src에 적용되어 출력되어진다.
 * 에디터에 내용입력시 적용되는 태그들이 detail페이지에서 태그가 html에 적용되지않고 태그그대로 출력되는 현상    
 -> 이스케이프에 관한 문제, thymeleaf의 utext사용하여 출력.
 * AWS서버에서 java의 mkdir()로 폴더 생성이 안되어 경로 문제 -> 서버 디렉토리의 권한모드 변경.

## 구동
<img src="/myboard/login3.PNG" width="400" height="250"></img>
<img src="/myboard/main1.PNG" width="400" height="250"></img>
<img src="/myboard/new1.PNG" width="400" height="250"></img>
<img src="/myboard/new2.PNG" width="400" height="250"></img>
<img src="/myboard/detail1.PNG" width="400" height="250"></img>
<img src="/myboard/detail2.PNG" width="400" height="250"></img>
<img src="/myboard/detailComment1.PNG" width="400" height="250"></img>
