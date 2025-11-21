# Google Drive-like Backend API

A Spring Boot backend application that provides Google Drive-like functionality for file and folder management.

## Features

- Create nested folders
- Upload files to folders
- List folder contents (subfolders and files)
- Download files
- Delete files and folders
- File storage on local disk
- PostgreSQL database for metadata

## Prerequisites

- Java 21
- Maven (or use included Maven wrapper)

## Setup

### 🚀 Quick Start (Using H2 Database - No PostgreSQL Required!)

The application is configured to use **H2 database** by default, so you can start immediately without installing PostgreSQL!

**Step 1: Start the application**
```bash
./start.sh
```

Or manually:
```bash
./mvnw spring-boot:run
```

**Step 2: Wait for the application to start** (you'll see "Started JpApplication" in the logs)

**Step 3: Test the API**
```bash
./test-api.sh
```

The application will start on `http://localhost:8080`

**🎯 Access Points:**
- API Base URL: http://localhost:8080/api
- H2 Database Console: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:./data/gdrive_db`
  - Username: `sa`
  - Password: (leave empty)

### Alternative: Using PostgreSQL (Optional)

If you want to use PostgreSQL instead:

1. **Install PostgreSQL** (via Homebrew on macOS):
   ```bash
   brew install postgresql@16
   brew services start postgresql@16
   ```

2. **Create database:**
   ```bash
   psql -U postgres -f database-setup.sql
   ```

3. **Update `pom.xml`**: Uncomment PostgreSQL dependency and comment out H2

4. **Update `application.properties`**: Uncomment PostgreSQL configuration and comment out H2

5. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

## API Endpoints

### Folder Operations

#### Create Folder
```http
POST /api/folders
Content-Type: application/json

{
  "name": "My Folder",
  "parentId": null  // null for root folder, or specify parent folder ID
}
```

#### Get Root Folder Contents
```http
GET /api/folders/root
```

#### Get Folder Contents
```http
GET /api/folders/{folderId}/contents
```

#### Delete Folder
```http
DELETE /api/folders/{folderId}
```

### File Operations

#### Upload File
```http
POST /api/files/upload
Content-Type: multipart/form-data

file: [binary file data]
folderId: [folder ID where file should be uploaded]
```

#### Download File
```http
GET /api/files/{fileId}/download
```

#### Delete File
```http
DELETE /api/files/{fileId}
```

## Example Usage with cURL

### Create a root folder
```bash
curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -d '{"name": "Documents", "parentId": null}'
```

### List root contents
```bash
curl http://localhost:8080/api/folders/root
```

### Upload a file
```bash
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@/path/to/your/file.pdf" \
  -F "folderId=1"
```

### Download a file
```bash
curl -O -J http://localhost:8080/api/files/1/download
```

### Delete a folder
```bash
curl -X DELETE http://localhost:8080/api/folders/1
```

## Project Structure

```
src/main/java/com/example/jp/
├── controller/
│   ├── FileController.java       # REST endpoints for file operations
│   └── FolderController.java     # REST endpoints for folder operations
├── dto/
│   ├── CreateFolderRequest.java  # Request DTO for folder creation
│   ├── FileItemDTO.java          # Response DTO for file metadata
│   ├── FolderContentsDTO.java    # Response DTO for folder contents
│   └── FolderDTO.java            # Response DTO for folder
├── entity/
│   ├── FileItem.java             # JPA entity for file metadata
│   └── Folder.java               # JPA entity for folder
├── repository/
│   ├── FileItemRepository.java   # Repository for file operations
│   └── FolderRepository.java     # Repository for folder operations
├── service/
│   ├── FileItemService.java      # Business logic for file operations
│   ├── FileStorageService.java   # Physical file storage operations
│   └── FolderService.java        # Business logic for folder operations
└── JpApplication.java            # Main application class
```

## Technologies Used

- **Spring Boot 4.0.0** - Application framework
- **Spring Data JPA** - Database access layer
- **PostgreSQL** - Database
- **Lombok** - Reduce boilerplate code
- **Maven** - Dependency management

## Notes

- Currently uses a hardcoded user ID: `"hardcoded-user-id"`
- Files are stored in the `uploads` directory (configurable via `app.upload.dir` property)
- Maximum file upload size: 100MB
- Database schema is auto-generated using Hibernate DDL auto-update

## Future Enhancements (v2)

- User authentication and authorization
- File sharing and permissions
- File versioning
- Search functionality
- Trash/recycle bin
- Cloud storage integration (S3, Google Cloud Storage)

