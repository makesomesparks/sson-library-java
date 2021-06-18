SSON library
====

Serializer와 JSON이 만났다! SSON 쏜!

spring boot를 학습하며 한 번 작성해본 라이브러리

# 1. 목표

Spring framework에서 RestAPI서비스를 개발할 경우 Model 객체등을 JSON형식으로 반환하는 경우가 다반사였다.

그러면서 다양한 요구사항이 있었는데 그 요구사항을 충족하는 라이브러리를 만들어보기로 했다.

사실 이와 충족하는 라이브러리들이 많았지만, 하나의 라이브러리가 아닌 여러가지 라이브러리를 의존해야 했으며, 

설정방법이 내가 사용하기에는 복잡하거나 어렵고, 소스코드를 읽기 어렵게 만들어버렸다.

그 요구사항들은 아래와 같았다.

# 1.1. 중요한 요구

- 설정파일은 단일 properties 하나로 만족한다

- 최대한 다른 JSON, Parser등의 라이브러리에 의존하지 않도록 한다

- Spring의 Processor와 연동하기 쉽도록 한다


# 1.2. Controller에서 Model에 파라미터 값 바인딩 받기

- Request Parameter 값을 받아올때 쓰일 수 있도록 한다.

- Request Parameter의 이름과 Field의 이름이 다르게 설정되도록 한다.

# 1.2. 객체를 Json String으로 변환

- Null일 경우 dispose 할 수 있도록 한다

- field가 Primitive Number 타입의 경우 integer, long, double, float등 sined, unsined 상관없이 type에 맞추어 expose한다.
  (간혹 return은 integer 인데 1.0, 0.0, 10.0 과 같이 expose되어 곤란할때가 많았다)

- field이름과 expose이름을 다르게 설정할 수 있어야 한다.

# 1.3. 세션에 맞추어 간단하게 사용

- expose 룰은 사용자의 세선에 맞추어 작동할 수 있도록 한다. 
  (예: 사용자의 정보를 가진 model의 경우 특정 field가 dispose, 소유자만 expose 되도록관리자만 출력, 또는 관리자만 보거나 무조건 보이지 않게 etc..)


# 1.4. 쉬운 Rule 적용

- 소스코드를 더 이상 조작하지않도록, 단순하게 Model의 Annotation 추가로 이루어지도록 한다



# 2. 진행사항

# 2.1 Annotation 

# 2.2 SSON !



# 3. 사용설명

# 4. 패키지별 설명

# 5. 기타