# Project Work Distribution for 6 Team Members

## 📊 Distribution Summary

| Person | Focus Area | Files Count | Complexity |
|--------|-----------|-------------|------------|
| **Person 1** | Core App + Config | 4 files | Medium |
| **Person 2** | Controllers & API | 3 files | Medium |
| **Person 3** | File Services | 3 files | High |
| **Person 4** | File Type Models (1) | 5 files | Medium |
| **Person 5** | File Type Models (2) | 4 files | Medium |
| **Person 6** | DTOs + Async Service | 6 files | High |

**Total: 25 Java files distributed equally**

---

## 👨‍💻 Person 1: Core Application & Configuration
**Role:** Application Setup & Threading Configuration Expert

### Files to Study (4 files):
```
1. src/main/java/com/example/jp/JpApplication.java
2. src/main/java/com/example/jp/config/SecurityConfig.java
3. src/main/java/com/example/jp/config/AsyncConfig.java
4. src/main/resources/application.properties
```

### Viva Topics to Prepare:
- ✅ Main application entry point and `@SpringBootApplication`
- ✅ How `@EnableAsync` enables asynchronous processing
- ✅ Security configuration (CSRF, permitAll)
- ✅ Thread pool configuration (core size, max size, queue capacity)
- ✅ Two executors: fileProcessingExecutor vs metadataExtractionExecutor
- ✅ Application properties (file size limits, storage path)
- ✅ Spring Boot architecture and dependency injection

### Key Questions You'll Face:
1. What does `@SpringBootApplication` do?
2. Explain the async configuration - why 2 executors?
3. What is thread pool core size vs max size?
4. Why disable CSRF for API endpoints?
5. What is the maximum file upload size?

---

## 👨‍💻 Person 2: Controllers & REST API
**Role:** API Endpoints & HTTP Request Handler Expert

### Files to Study (3 files):
```
1. src/main/java/com/example/jp/controller/FileController.java
2. src/main/java/com/example/jp/controller/FolderController.java
3. src/main/java/com/example/jp/controller/AsyncFileController.java
```

### Viva Topics to Prepare:
- ✅ REST API principles (GET, POST, DELETE)
- ✅ `@RestController`, `@RequestMapping`, `@RequestParam`
- ✅ File upload with `MultipartFile`
- ✅ Synchronous vs Asynchronous endpoints
- ✅ HTTP status codes (200, 202, 204, 404, 500)
- ✅ Difference between `/api/files/upload` and `/api/async/files/upload`
- ✅ Batch upload implementation
- ✅ Task tracking with taskId
- ✅ `CompletableFuture` for async responses

### Key Questions You'll Face:
1. What's the difference between sync and async upload?
2. Explain the batch upload endpoint
3. How do you track async task status?
4. What is `CompletableFuture`?
5. Why return HTTP 202 for async operations?
6. Explain `@RequestParam` vs `@PathVariable`

---

## 👨‍💻 Person 3: File & Folder Services
**Role:** Business Logic & Storage Operations Expert

### Files to Study (3 files):
```
1. src/main/java/com/example/jp/service/FileItemService.java
2. src/main/java/com/example/jp/service/FolderService.java
3. src/main/java/com/example/jp/service/FileStorageService.java
```

### Viva Topics to Prepare:
- ✅ Service layer pattern and business logic
- ✅ File system operations (Files.copy, Files.list, Files.delete)
- ✅ Java NIO (Path, Paths, BasicFileAttributes)
- ✅ How files are stored with original names
- ✅ Duplicate file handling (adding numbers)
- ✅ Recursive folder deletion
- ✅ Metadata extraction (size, created/updated timestamps)
- ✅ Factory pattern usage for creating file types
- ✅ DTO conversion

### Key Questions You'll Face:
1. How do you handle duplicate file names?
2. Explain recursive folder deletion
3. How do you get file metadata?
4. What is Java NIO and why use it?
5. How does the factory pattern work here?
6. Explain folder listing implementation

---

## 👨‍💻 Person 4: File Type Models - Part 1
**Role:** Inheritance & OOP Expert (Documents, Images, Videos)

### Files to Study (5 files):
```
1. src/main/java/com/example/jp/model/FileItem.java (Abstract base class)
2. src/main/java/com/example/jp/model/DocumentFile.java
3. src/main/java/com/example/jp/model/ImageFile.java
4. src/main/java/com/example/jp/model/VideoFile.java
5. src/main/java/com/example/jp/model/FileItemFactory.java
```

### Viva Topics to Prepare:
- ✅ **Inheritance hierarchy** (abstract FileItem → concrete subclasses)
- ✅ **Abstract classes** and abstract methods
- ✅ **Polymorphism** - same method, different implementations
- ✅ `getFileTypeCategory()` and `getFileTypeDescription()`
- ✅ **Factory pattern** - creating appropriate file type based on extension
- ✅ DocumentFile: PDF, DOCX, TXT properties (pageCount, isEditable)
- ✅ ImageFile: dimensions, transparency support
- ✅ VideoFile: duration, resolution, codec
- ✅ File extension detection

### Key Questions You'll Face:
1. **Explain inheritance in this project**
2. Why is FileItem abstract?
3. What is polymorphism? Give example from code
4. How does Factory pattern work?
5. What makes ImageFile different from DocumentFile?
6. Explain abstract methods vs concrete methods

---

## 👨‍💻 Person 5: File Type Models - Part 2
**Role:** OOP & Specialized File Types Expert (Audio, Archive, Generic, Folder)

### Files to Study (4 files):
```
1. src/main/java/com/example/jp/model/AudioFile.java
2. src/main/java/com/example/jp/model/ArchiveFile.java
3. src/main/java/com/example/jp/model/GenericFile.java
4. src/main/java/com/example/jp/model/Folder.java
```

### Viva Topics to Prepare:
- ✅ AudioFile: MP3, WAV, FLAC (duration, bitrate, metadata)
- ✅ ArchiveFile: ZIP, RAR (compression ratio, file count)
- ✅ GenericFile: Fallback for unknown types
- ✅ Folder model (separate from FileItem)
- ✅ How each file type extends FileItem
- ✅ Specialized properties for each type
- ✅ Duration formatting (minutes:seconds)
- ✅ Compression ratio calculation

### Key Questions You'll Face:
1. Why do we need GenericFile class?
2. Explain AudioFile specific properties
3. How is compression ratio calculated?
4. Why is Folder separate from FileItem?
5. What file types does each class handle?
6. Show inheritance hierarchy

---

## 👨‍💻 Person 6: DTOs & Async Processing
**Role:** Data Transfer & Asynchronous Operations Expert

### Files to Study (6 files):
```
1. src/main/java/com/example/jp/dto/FileItemDTO.java
2. src/main/java/com/example/jp/dto/FolderDTO.java
3. src/main/java/com/example/jp/dto/FolderContentsDTO.java
4. src/main/java/com/example/jp/dto/CreateFolderRequest.java
5. src/main/java/com/example/jp/dto/FileUploadTaskDTO.java
6. src/main/java/com/example/jp/service/AsyncFileProcessingService.java
7. src/main/java/com/example/jp/model/FileUploadTask.java
```

### Viva Topics to Prepare:
- ✅ **DTO Pattern** - why separate from models?
- ✅ Request vs Response DTOs
- ✅ `@Async` annotation and how it works
- ✅ Thread pool executors
- ✅ Task tracking with ConcurrentHashMap
- ✅ `CompletableFuture` for async returns
- ✅ Task status (PENDING, IN_PROGRESS, COMPLETED, FAILED)
- ✅ Progress tracking (0-100%)
- ✅ Background processing
- ✅ Thread safety with concurrent collections

### Key Questions You'll Face:
1. What is DTO pattern and why use it?
2. Explain `@Async` annotation
3. How does async upload work?
4. What is ConcurrentHashMap and why use it?
5. Explain task status lifecycle
6. How do you track progress?
7. What is CompletableFuture?

---

## 🎯 Common Topics All Should Know

### Everyone Should Be Able to Explain:
1. **Project Overview**: File management system with OOP
2. **Total Classes**: 28 classes
3. **Main Features**: Upload, download, folders, async processing
4. **OOP Concepts**: Inheritance, abstraction, polymorphism, encapsulation
5. **Design Patterns**: Factory, DTO, Service Layer
6. **Threading**: Async processing with thread pools
7. **Storage**: File system (no database)
8. **API**: RESTful with sync and async endpoints

---

## 📝 Distribution Justification

### Why This Split is Fair:

| Person | Complexity | LOC | Concepts |
|--------|-----------|-----|----------|
| Person 1 | Medium | ~150 | Spring Boot, Config, Threading |
| Person 2 | Medium | ~200 | REST API, HTTP, Async |
| Person 3 | High | ~350 | File I/O, NIO, Business Logic |
| Person 4 | Medium | ~300 | Inheritance, Polymorphism, Factory |
| Person 5 | Medium | ~250 | OOP, Specialized Types |
| Person 6 | High | ~350 | DTO, Async, Concurrency |

**Balanced by:**
- ✅ Number of files (3-6 each)
- ✅ Complexity level (mixed high/medium)
- ✅ Related concepts (grouped logically)
- ✅ Lines of code (~200-350 each)

---

## 🎓 Viva Preparation Tips

### For Each Person:

1. **Read your files thoroughly** (2-3 times)
2. **Understand every line** - don't just memorize
3. **Draw diagrams** (class hierarchy, flow charts)
4. **Practice explaining** to someone else
5. **Know related concepts** from other modules
6. **Prepare examples** from your files
7. **Know imports** - why each library is used

### Cross-Team Questions to Expect:
- "How does your module interact with [Person X]'s module?"
- "Explain the flow from API call to file storage"
- "Which design pattern did you use and why?"
- "What OOP principle is demonstrated in your code?"

---

## 📞 Quick Reference Card for Each Person

### Person 1 (Config):
**I handle**: Application startup, security, thread pools  
**Key concept**: Configuration & threading  
**Connect to**: Person 6 (uses my async config), Person 2 (uses my security)

### Person 2 (Controllers):
**I handle**: API endpoints, HTTP requests  
**Key concept**: REST API & routing  
**Connect to**: Person 3 (calls services), Person 6 (uses DTOs & async service)

### Person 3 (Services):
**I handle**: Business logic, file operations  
**Key concept**: Service layer & file I/O  
**Connect to**: Person 4/5 (creates file models), Person 6 (converts to DTOs)

### Person 4 (Models 1):
**I handle**: File type inheritance (Docs, Images, Videos)  
**Key concept**: Inheritance & polymorphism  
**Connect to**: Person 5 (other file types), Person 3 (used by services)

### Person 5 (Models 2):
**I handle**: More file types (Audio, Archive, Folder)  
**Key concept**: OOP & specialized types  
**Connect to**: Person 4 (inheritance hierarchy), Person 3 (used by services)

### Person 6 (DTOs & Async):
**I handle**: Data transfer, async processing  
**Key concept**: DTO pattern & threading  
**Connect to**: Person 2 (provides DTOs), Person 1 (uses async config), Person 3 (wraps service responses)

---

## 🚀 Final Advice

1. **Meet as a team** - understand overall flow
2. **Each person becomes expert** in their area
3. **Help each other** - cross-review
4. **Practice together** - mock viva sessions
5. **Be ready to explain** how pieces connect

**Good luck with your viva! 🎓**
