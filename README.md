# 생활 중국어 암기카드 앱 — Android Studio 빌드 가이드

## 앱 기능
- 생활 중국어 25개 단어 (인사·쇼핑·음식·교통·일상 5개 카테고리)
- 카드 앞면: 한자 + 병음(성조 색상) / 뒷면: 한국어 뜻
- 탭으로 카드 뒤집기
- 맞힘 ✅ / 틀림 ❌ 체크 → 취약 카드 자동 분류
- 취약 카드만 반복 학습 모드
- 중국어 TTS (zh-CN) + 한국어 TTS (ko-KR) 내장
- 성조 색상: 1성=빨강, 2성=주황, 3성=초록, 4성=파랑, 경성=회색

## 빌드 방법

### 사전 준비
1. Android Studio 설치: https://developer.android.com/studio
2. Android SDK 34 설치 (Android Studio에서 자동 설치 가능)

### 빌드 단계
1. Android Studio 실행
2. "Open" → `ChineseFlashcard` 폴더 선택
3. Gradle Sync 완료 대기 (최초 2~5분)
4. Build → Build Bundle(s) / APK(s) → Build APK(s)
5. `app/build/outputs/apk/debug/app-debug.apk` 파일 생성됨

### 기기 설치
- USB 연결 후: `adb install app-debug.apk`
- 또는 APK 파일을 기기로 전송 후 직접 설치 (설정에서 "알 수 없는 소스 허용" 필요)

## 폴더 구조
```
ChineseFlashcard/
├── app/
│   ├── src/main/
│   │   ├── java/com/flashcard/chinese/
│   │   │   ├── MainActivity.java          ← 홈 화면
│   │   │   ├── FlashcardActivity.java     ← 카드 학습
│   │   │   ├── ResultActivity.java        ← 결과 화면
│   │   │   ├── WordData.java              ← 단어 모델
│   │   │   ├── WordRepository.java        ← 25개 단어 + 저장
│   │   │   ├── TtsManager.java            ← TTS 관리
│   │   │   └── ToneColorizer.java         ← 성조 색상
│   │   ├── res/layout/                    ← UI 레이아웃
│   │   └── AndroidManifest.xml
│   └── build.gradle
└── README.md
```

## TTS 주의사항
- 기기에 zh-CN(중국어 간체) 언어팩이 설치되어 있어야 중국어 음성 출력
- 없을 경우: 설정 → 일반 관리 → 언어 → 텍스트 음성 출력 → 언어 추가 → 중국어(간체)
- ko-KR은 삼성/구글 기기 대부분 기본 탑재
