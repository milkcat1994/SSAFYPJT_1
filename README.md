## Introduction

#### 편하게 집에서 의뢰하자!
**편집자**와 **의뢰자**와의 매칭을 도와주는 사이트입니다.

**편집**에서 좋은 인연을 얻어가세요.

#### Description
- PC로 편집자를 선택할 수 있는 사이트 입니다.
- 편집자와 비편집자를 선택하여 사이트 회원가입이 가능합니다.
- 편집자 검색의 경우 비회원으로도 가능하지만 의뢰 및 부가적인 기능은 회원가입 후 이용이 가능합니다.
- 회원가입시 북마크, 편집자 추천, 편집 의뢰 등이 가능합니다.
- 편집자는 포트폴리오 페이지를 통해 본인을 어필할 수 있습니다.
- '맞춤 편집자 찾기'를 통해 편집자를 찾고 작업 요청까지 보낼 수 있습니다.
- '전체 편집자 찾기'를 통해 여러 검색이 가능합니다.
- 알람 페이지에서 알람과 일정 모두를 확인할 수 있습니다.
- 참고: [크몽](https://kmong.com/), [숨고](https://soomgo.com/)



## Team
### R&R
|      | :cat:양지용                                                  | :crown:전수현                                                | :mouse:정세린                                                |
| ---- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 기능 | AWS및 환경설정, 다중 DB구축, Redis 구축 및 예외처리          | 편집자의 포트폴리오 (등록, 수정, 확인)페이지 (FE, BE), 요청서(FE), 서버 배포 | 구글로그인, 알림 페이지(FE, BE), 요청서(BE), CF-Algorithm이용한 편집자 추천, |
|      | :tiger:홍주표                                                | :rabbit:조다빈                                               | :hamster:임진희                                              |
| 기능 | 맞춤 편집자 찾기 페이지(FE), 편집자 검색 페이지(FE), UCC제작 | 회원관리, 메인 페이지 북마크 페이지(FE), 후기(FE), 편집자 검색 페이지(FE), AboutUs 페이지 | 회원관리(BE), PPT 및 문서작업, 더미데이터 생성               |



## Architecture
![Architecture](https://user-images.githubusercontent.com/18321002/98938118-c0e4b400-252a-11eb-83da-ca2415791fa6.png)

## Environment
### FRONT
- Vue (2.6.11)
- Bootstrap (4.5.0)
- Vuex (3.5.1)
- Node.js (14.0.0)
### BACK
- Spring-boot (2.2.3.RELEASE)
- Lombok
- MyBatis (1.3.2)
- Redis
- javax.mail (1.4.7)
- gson (2.8.5)
### CI/CD
- Jenkins
### SERVER
- Docker
### DB
- MySQL
### VCS(Version Championship Series)
- Gitlab
- Git Flow 
## Directory
### Frontend
<details>
  <summary>펼치기 / 접기</summary>
  <div>

```
📁frontend
└── 📁src
    ├── 📁assets
    ├── 📁components
    │   ├── 📁NotificationPlugin
    │   └── 📁SidebarPlugin
    ├── 📁layout
    ├── 📁plugins
    ├── 📁store
    ├── 📁util
    ├── 📁views
    │   ├── 📁Dashboard
    │   ├── 📁Error
    │   ├── 📁Portfolio
    │   ├── 📁Search
    │   └── 📁Tables
    └── App
```

  </div>
</details>

### Backend
<details>
  <summary>펼치기 / 접기</summary>
  <div>

```
📁backend
└── 📁src
    ├── 📁main
    │   ├── 📁java/com/web/editor
    │   │   ├── 📁config
    │   │   ├── 📁controller
    │   │   │   ├── 📁email
    │   │   │   ├── 📁google
    │   │   │   ├── 📁kakao
    │   │   │   ├── 📁request
    │   │   │   └── 📁user
    │   │   ├── 📁model
    │   │   │   ├── 📁dto
    │   │   │   ├── 📁mapper
    │   │   │   ├── 📁response
    │   │   │   └── 📁service
    │   │   └── MainApplication
    │   └── 📁resources
    └── 📁test
```

  </div>
</details>

## Core Skills
- Google Mail Authentication Service
- Item-based recommendation Algorithm (CF-Algorithm)
- Separation of database by function
- Redis (Cache Server)
    - Remember editors based on search condition
- HA(High Availabilty)
    - don't connections to Redis, connect directly to the database.

## Document
### [Daily Meeting contents](https://docs.google.com/document/d/1xnoydTo05h0HvReFcqFpp50SUzsqPLf2my8h0uzs764/edit?usp=sharing)
### [Release Note](https://github.com/SSAFYPJT_1/blob/master/wiki/Release-Notes.md)
### [WireFrame](https://github.com/SSAFYPJT_1/blob/master/wiki/와이어프레임.md)
### [Wiki](https://github.com/SSAFYPJT_1/blob/master/wiki/home.md)
### [ERD](https://github.com/SSAFYPJT_1/blob/master/wiki/ERD-설계.md)

## Project UCC
📺[UCC](https://youtu.be/_M6JkZcUDF0)

## License
[MIT license](https://github.com/connect-foundation/2019-15/blob/master/LICENSE)

## Copyright
Copyright 2020. 편집. All rights reserved.

## Show your support
Give a ⭐️ if this project helped you!

---

\_This README was generated with ❤️

