# JP - File Management System

A production-ready Spring Boot backend application that provides Google Drive-like functionality for file and folder management. This project demonstrates advanced OOP principles including inheritance, polymorphism, abstraction, threading, and design patterns.

## 🎯 Project Overview

This is a comprehensive file management system built for academic purposes that showcases:
- **Object-Oriented Programming** with proper inheritance hierarchy
- **Asynchronous processing** with thread pools
- **RESTful API design** with synchronous and asynchronous endpoints
- **File system operations** without database dependencies
- **Design patterns** (Factory, Strategy, Async)
- **Clean architecture** with proper separation of concerns

## ✨ Features

### Core Functionality
- ✅ Create nested folders with actual directory structure
- ✅ Upload files with original names (single & batch)
- ✅ Asynchronous file processing with progress tracking
- ✅ List folder contents (subfolders and files)
- ✅ Download files with proper content types
- ✅ Delete files and folders (with cascade)
- ✅ Automatic duplicate file handling

### Technical Features
- ✅ **No database required** - pure file system storage
- ✅ **File type inheritance** - 6 specialized file classes
- ✅ **Async processing** - non-blocking uploads with thread pools
- ✅ **Task tracking** - real-time upload progress monitoring
- ✅ **Thread safety** - concurrent operations with ConcurrentHashMap
- ✅ **REST API** - comprehensive endpoint coverage
- ✅ **Type detection** - automatic file categorization

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.6+ (or use included Maven wrapper)
- macOS/Linux/Windows
- No database installation required!

## 🚀 Quick Start

### 1. Clone and Build
```bash
cd JP
./mvnw clean compile
```

### 2. Run the Application
```bash
./mvnw spring-boot:run
```

Or use the startup script:
```bash
./start.sh
```

### 3. Verify Installation
The application will start on `http://localhost:8080`

```bash
# Test basic endpoints
curl http://localhost:8080/api/folders/root

# Should return: {"folders":[],"files":[]}
```

### 4. Run Test Suite
```bash
# Test synchronous file operations
./test-filesystem-api.sh

# Test asynchronous file operations
./test-async-api.sh
```

## 📂 Project Structure

```
JP/
├── src/main/java/com/example/jp/
│   ├── JpApplication.java              # Main application entry point
│   ├── config/
│   │   ├── AsyncConfig.java            # Thread pool configuration
│   │   └── SecurityConfig.java         # Security settings
│   ├── controller/
│   │   ├── FileController.java         # Sync file operations API
│   │   ├── FolderController.java       # Folder management API
│   │   └── AsyncFileController.java    # Async file operations API
│   ├── service/
│   │   ├── FileItemService.java        # File business logic
│   │   ├── FolderService.java          # Folder business logic
│   │   ├── FileStorageService.java     # Physical file storage
│   │   └── AsyncFileProcessingService.java # Async processing
│   ├── model/
│   │   ├── FileItem.java               # Abstract base class
│   │   ├── DocumentFile.java           # PDF, DOC, TXT, etc.
│   │   ├── ImageFile.java              # JPG, PNG, GIF, etc.
│   │   ├── VideoFile.java              # MP4, AVI, MOV, etc.
│   │   ├── AudioFile.java              # MP3, WAV, FLAC, etc.
│   │   ├── ArchiveFile.java            # ZIP, RAR, 7Z, etc.
│   │   ├── GenericFile.java            # Other file types
│   │   ├── FileItemFactory.java        # Factory pattern
│   │   ├── Folder.java                 # Folder model
│   │   └── FileUploadTask.java         # Async task tracking
│   └── dto/
│       ├── FileItemDTO.java            # File response DTO
│       ├── FolderDTO.java              # Folder response DTO
│       ├── FolderContentsDTO.java      # Folder contents DTO
│       ├── CreateFolderRequest.java    # Folder creation request
│       └── FileUploadTaskDTO.java      # Task status DTO
├── storage/                             # File storage (auto-created)
├── test-filesystem-api.sh               # Sync API tests
├── test-async-api.sh                    # Async API tests
└── pom.xml                              # Maven dependencies
```

## 🎯 Architecture & Design

### OOP Principles Demonstrated

#### 1. **Inheritance Hierarchy**
```
FileItem (Abstract)
├── DocumentFile (PDF, DOCX, TXT)
├── ImageFile (JPG, PNG, GIF)
├── VideoFile (MP4, AVI, MOV)
├── AudioFile (MP3, WAV, FLAC)
├── ArchiveFile (ZIP, RAR, 7Z)
└── GenericFile (Other types)
```

#### 2. **Abstraction**
- Abstract `FileItem` class with abstract methods
- `getFileTypeCategory()` and `getFileTypeDescription()` implemented by each subclass

#### 3. **Polymorphism**
- Different file types respond differently to same method calls
- Factory pattern creates appropriate subclass instances

#### 4. **Encapsulation**
- Private fields with public getters/setters
- Service layer encapsulates business logic
- DTOs separate internal models from API responses

#### 5. **Design Patterns**
- **Factory Pattern**: `FileItemFactory` creates appropriate file types
- **Strategy Pattern**: Different file type implementations
- **Async Pattern**: Non-blocking operations with CompletableFuture
- **DTO Pattern**: Separation of concerns

### Threading Architecture

- **File Processing Executor**: 5-10 threads for file uploads
- **Metadata Extraction Executor**: 3-6 threads for background processing
- **Task Management**: ConcurrentHashMap for thread-safe task tracking
- **CompletableFuture**: Non-blocking async operations

## 📡 API Documentation

### Synchronous File Operations

#### Upload File
```bash
POST /api/files/upload
Content-Type: multipart/form-data

Parameters:
  - file: File to upload
  - folderPath: Target folder (empty for root)

Response:
{
  "name": "document.pdf",
  "path": "Documents/document.pdf",
  "size": 1048576,
  "mimeType": "application/pdf",
  "fileTypeCategory": "DOCUMENT",
  "fileTypeDescription": "PDF Document",
  "extension": "pdf",
  "createdAt": "2025-11-25T10:00:00",
  "updatedAt": "2025-11-25T10:00:00"
}
```

#### Download File
```bash
GET /api/files/download?path=Documents/document.pdf

Response: Binary file stream with proper content-type
```

#### Delete File
```bash
DELETE /api/files?path=Documents/document.pdf

Response: 204 No Content
```

### Folder Operations

#### Create Folder
```bash
POST /api/folders
Content-Type: application/json

Body:
{
  "name": "Documents",
  "parentPath": ""
}

Response:
{
  "id": null,
  "name": "Documents",
  "path": "Documents",
  "parentId": null,
  "createdAt": "2025-11-25T10:00:00",
  "updatedAt": "2025-11-25T10:00:00"
}
```

#### Get Folder Contents
```bash
GET /api/folders/root
GET /api/folders/contents?path=Documents

Response:
{
  "folders": [...],
  "files": [...]
}
```

#### Delete Folder
```bash
DELETE /api/folders?path=Documents/Old

Response: 204 No Content
```

### Asynchronous File Operations

#### Async Upload (Non-blocking)
```bash
POST /api/async/files/upload
Content-Type: multipart/form-data

Parameters:
  - file: File to upload
  - folderPath: Target folder

Response (202 Accepted):
{
  "taskId": "550e8400-e29b-41d4-a716-446655440000",
  "message": "File upload started",
  "statusUrl": "/api/async/files/status/550e8400..."
}
```

#### Check Upload Status
```bash
GET /api/async/files/status/{taskId}

Response:
{
  "taskId": "550e8400-e29b-41d4-a716-446655440000",
  "fileName": "large-file.zip",
  "status": "IN_PROGRESS",
  "progressPercent": 75,
  "message": "Processing...",
  "startTime": "2025-11-25T10:00:00"
}
```

#### Batch Upload (Multiple Files)
```bash
POST /api/async/files/batch-upload
Content-Type: multipart/form-data

Parameters:
  - files[]: Multiple files
  - folderPath: Target folder

Response (202 Accepted):
{
  "totalFiles": 3,
  "taskIds": {
    "file1.pdf": "taskId1",
    "file2.jpg": "taskId2",
    "file3.txt": "taskId3"
  }
}
```

#### Sync Async Upload (Wait for completion)
```bash
POST /api/async/files/upload-sync
Content-Type: multipart/form-data

Parameters:
  - file: File to upload
  - folderPath: Target folder

Response (200 OK - after completion):
{
  "name": "document.pdf",
  "path": "Documents/document.pdf",
  ...
}
```

## API Endpoints

### Folder Operations

#### Create Folder
```http
POST /api/folders
Content-Type: application/json

{
  "name": "My Folder",
  "parentPath": ""  // Empty for root, or "Documents" for nested folder
}
```

#### Get Root Folder Contents
```http
GET /api/folders/root
```

#### Get Folder Contents by Path
```http
GET /api/folders/contents?path=Documents
GET /api/folders/contents?path=Documents/Work
```

#### Delete Folder
```http
DELETE /api/folders?path=Documents/Work
```

### File Operations

#### Upload File
```http
POST /api/files/upload
Content-Type: multipart/form-data

file: [binary file data]
folderPath: ""  // Empty for root, or "Documents" for specific folder
```

#### Download File
```http
GET /api/files/download?path=Documents/myfile.pdf
```

#### Delete File
```http
DELETE /api/files?path=Documents/myfile.pdf
```

## 💡 Usage Examples

### Basic Workflow

```bash
# 1. Create a folder structure
curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -d '{"name": "Documents", "parentPath": ""}'

curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -d '{"name": "Work", "parentPath": "Documents"}'

# 2. Upload files
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@report.pdf" \
  -F "folderPath=Documents/Work"

# 3. List contents
curl http://localhost:8080/api/folders/contents?path=Documents/Work

# 4. Download file
curl -O -J "http://localhost:8080/api/files/download?path=Documents/Work/report.pdf"
```

### Async Upload Workflow

```bash
# 1. Start async upload
RESPONSE=$(curl -s -X POST http://localhost:8080/api/async/files/upload \
  -F "file=@large-video.mp4" \
  -F "folderPath=Videos")

TASK_ID=$(echo $RESPONSE | jq -r '.taskId')
echo "Task ID: $TASK_ID"

# 2. Check progress
curl http://localhost:8080/api/async/files/status/$TASK_ID

# 3. Poll until complete
while true; do
  STATUS=$(curl -s http://localhost:8080/api/async/files/status/$TASK_ID | jq -r '.status')
  if [ "$STATUS" = "COMPLETED" ]; then
    echo "Upload complete!"
    break
  fi
  sleep 1
done
```

### Batch Upload

```bash
# Upload multiple files in parallel
curl -X POST http://localhost:8080/api/async/files/batch-upload \
  -F "files=@photo1.jpg" \
  -F "files=@photo2.jpg" \
  -F "files=@photo3.jpg" \
  -F "folderPath=Photos"
```

## 🔧 Technologies & Dependencies

### Core Technologies
- **Java 21** - Latest LTS with modern language features
- **Spring Boot 4.0.0** - Application framework
- **Spring Web** - REST API implementation
- **Spring Security** - Security configuration
- **Spring Async** - Asynchronous processing
- **Java NIO** - File system operations

### Libraries
- **Lombok** - Reduce boilerplate code
- **Jackson** - JSON processing
- **SLF4J** - Logging facade
- **Maven** - Build and dependency management

### Architecture Patterns
- **MVC Pattern** - Model-View-Controller separation
- **Factory Pattern** - Dynamic object creation
- **DTO Pattern** - Data transfer objects
- **Service Layer Pattern** - Business logic separation
- **Async Pattern** - Non-blocking operations

## 📊 Project Statistics

- **Total Classes**: 28
- **Controllers**: 3 (REST API endpoints)
- **Services**: 4 (Business logic)
- **Models**: 10 (8 file types + 2 others)
- **DTOs**: 5 (Data transfer objects)
- **Configuration**: 2 (Security + Async)
- **Design Patterns**: 4+ implemented
- **API Endpoints**: 15+ REST endpoints
- **Lines of Code**: ~2000+

## 🎓 Academic Features

This project demonstrates the following OOP and advanced programming concepts:

### Object-Oriented Programming
✅ **Inheritance** - 6-level file type hierarchy  
✅ **Abstraction** - Abstract base classes with abstract methods  
✅ **Polymorphism** - Runtime method binding  
✅ **Encapsulation** - Private fields with controlled access  

### Advanced Concepts
✅ **Multithreading** - Thread pools and concurrent execution  
✅ **Asynchronous Programming** - Non-blocking I/O operations  
✅ **Design Patterns** - Factory, Strategy, DTO, Singleton  
✅ **Dependency Injection** - Constructor-based DI  
✅ **RESTful API Design** - Standard HTTP methods  
✅ **Exception Handling** - Try-catch and error responses  
✅ **Logging** - SLF4J with log levels  
✅ **Thread Safety** - ConcurrentHashMap usage  

## 📝 Configuration

### Application Properties
```properties
# File upload settings
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB

# Storage location
app.storage.root=storage
```

### Thread Pool Configuration
- **File Processing Pool**: 5-10 threads, 100 queue capacity
- **Metadata Extraction Pool**: 3-6 threads, 50 queue capacity
- **Thread Names**: Prefixed for easy debugging

## 🗂️ File Type Support

| Category | Extensions | Special Properties |
|----------|-----------|-------------------|
| **Documents** | pdf, doc, docx, txt, rtf, odt, xls, xlsx, ppt, pptx, csv | Page count, editability |
| **Images** | jpg, jpeg, png, gif, svg, bmp, webp, ico, tiff | Dimensions, transparency |
| **Videos** | mp4, avi, mov, mkv, wmv, flv, webm, mpeg | Duration, resolution, codec |
| **Audio** | mp3, wav, flac, aac, ogg, m4a, wma | Duration, bitrate, metadata |
| **Archives** | zip, rar, 7z, tar, gz, bz2, xz | File count, compression |
| **Generic** | All others | Basic metadata only |

## 🧪 Testing

### Automated Test Scripts

1. **test-filesystem-api.sh** - Tests synchronous operations
   - Folder creation
   - File upload/download
   - Content listing
   - Deletion operations

2. **test-async-api.sh** - Tests asynchronous operations
   - Async uploads
   - Status tracking
   - Batch uploads
   - Progress monitoring

### Manual Testing

```bash
# Start the application
./mvnw spring-boot:run

# In another terminal, run tests
./test-filesystem-api.sh
./test-async-api.sh

# Check the storage directory
ls -la storage/
```

## 📁 Storage Structure

```
storage/
├── Documents/
│   ├── report.pdf          # Real file with original name
│   ├── notes.txt
│   └── Work/
│       ├── project.docx
│       └── budget.xlsx
├── Photos/
│   ├── vacation.jpg
│   ├── family.png
│   └── screenshot (1).png  # Auto-numbered duplicate
└── Videos/
    └── demo.mp4
```

## 🚀 Performance

- **Concurrent Uploads**: Up to 10 simultaneous file uploads
- **Thread Pooling**: Efficient resource utilization
- **Non-blocking I/O**: Improved throughput
- **Queue Management**: 100+ tasks can be queued
- **Graceful Shutdown**: Waits for tasks to complete

## 🔒 Security

- **CSRF Protection**: Disabled for API endpoints
- **CORS**: Can be configured for frontend integration
- **File Validation**: Size limits (100MB default)
- **Path Traversal Protection**: Safe path handling
- **Thread Safety**: Concurrent operation support

## 🐛 Troubleshooting

### Port Already in Use
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9
```

### Storage Permission Issues
```bash
# Ensure storage directory is writable
chmod 755 storage/
```

### Async Tasks Not Processing
- Check thread pool configuration in `AsyncConfig`
- Verify `@EnableAsync` is present
- Check logs for thread pool saturation

### File Upload Fails
- Verify file size < 100MB
- Check disk space
- Ensure storage directory exists and is writable

## 📚 Additional Documentation

- `INHERITANCE_IMPLEMENTATION.md` - Detailed inheritance hierarchy
- `FILESYSTEM_MIGRATION.md` - Migration from H2 to file system
- `test-filesystem-api.sh` - Sync API test suite
- `test-async-api.sh` - Async API test suite

## 🎯 Future Enhancements

### Planned Features
- [ ] User authentication with JWT
- [ ] File sharing with permissions
- [ ] Thumbnail generation for images
- [ ] Video metadata extraction
- [ ] Full-text search
- [ ] Trash/Recycle bin
- [ ] File versioning
- [ ] Cloud storage integration (S3)
- [ ] Real-time progress via WebSockets
- [ ] Compression/Decompression API

## 👨‍💻 Development

### Build
```bash
./mvnw clean package
```

### Run Tests
```bash
./mvnw test
```

### Generate JAR
```bash
./mvnw clean package
java -jar target/JP-0.0.1-SNAPSHOT.jar
```

## 📄 License

This project is created for academic purposes.

## 🤝 Contributing

This is an academic project. For suggestions or improvements, please create an issue or pull request.

---

**Project Author**: Abdul Farooqui  
**Course**: Object-Oriented Programming  
**Total Classes**: 28  
**Design Patterns**: Factory, Strategy, DTO, Async  
**Key Features**: Inheritance, Threading, REST API, File Management

---

⭐ **Star this repository if you find it helpful for learning OOP concepts!**

