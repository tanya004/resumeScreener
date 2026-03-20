# AI Resume Screener 🤖

A GenAI-powered resume screening tool built with Java Spring Boot and Google Gemini API. Upload a PDF resume and a job description — get instant AI analysis with a match score, strengths, missing skills, and hiring recommendation.

## Tech Stack

- **Java 21** + **Spring Boot 3.2**
- **Google Gemini API** (gemini-2.5-flash) — AI analysis
- **Apache PDFBox** — PDF text extraction
- **Spring WebFlux** — Async HTTP client
- **H2 Database** — In-memory storage
- **Spring Data JPA** — ORM

## Features

- Upload PDF resumes via REST API
- Extracts text from PDF automatically
- AI-powered analysis against job description
- Match score (0-100), strengths, missing skills, recommendation
- Stores results in database
- View all screened resumes

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/resumes/upload` | Upload resume + job description |
| GET | `/api/resumes` | Get all screened resumes |
| GET | `/api/resumes/{id}` | Get resume by ID |

## Getting Started

### Prerequisites
- Java 21+
- Maven

### Setup

1. Clone the repository
```bash
git clone https://github.com/tanya004/resume-screener.git
cd resume-screener
```

2. Copy the example properties file
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

3. Add your Gemini API key in `application.properties`
```properties
gemini.api.key=your_gemini_api_key_here
```
Get your free API key at: https://aistudio.google.com/app/apikey

4. Run the app
```bash
./mvnw spring-boot:run
```

5. App runs at `http://localhost:8080`

### Testing with Postman

- Method: `POST`
- URL: `http://localhost:8080/api/resumes/upload`
- Body: `form-data`
    - `file` (File) — PDF resume
    - `jobDescription` (Text) — Job description text

## H2 Database Console

Access at `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:resume_screener`
- Username: `sa`
- Password: (leave empty)

## Author

Tanya Singh — [GitHub](https://github.com/tanya004) | 
