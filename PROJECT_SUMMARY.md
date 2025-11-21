# 🎉 Google Drive-like Backend - Project Complete!

## ✅ What Has Been Built

You now have a fully functional Google Drive-like backend with:

### **Core Features:**
- ✅ Create nested folders (unlimited depth)
- ✅ Upload files to folders
- ✅ List folder contents (subfolders + files)
- ✅ Download files
- ✅ Delete files and folders
- ✅ File storage on local disk
- ✅ H2 Database (no PostgreSQL installation needed!)

---

## 📁 Project Structure

```
JP/
├── src/main/java/com/example/jp/
│   ├── controller/
│   │   ├── FileController.java       # File upload/download/delete endpoints
│   │   └── FolderController.java     # Folder CRUD endpoints
│   ├── dto/
│   │   ├── CreateFolderRequest.java  # Request for folder creation
│   │   ├── FileItemDTO.java          # File metadata response
│   │   ├── FolderContentsDTO.java    # Combined folder listing
│   │   └── FolderDTO.java            # Folder response
│   ├── entity/
│   │   ├── FileItem.java             # File metadata entity
│   │   └── Folder.java               # Folder entity (supports nesting)
│   ├── repository/
│   │   ├── FileItemRepository.java   # File data access
│   │   └── FolderRepository.java     # Folder data access
│   ├── service/
│   │   ├── FileItemService.java      # File business logic
│   │   ├── FileStorageService.java   # Physical file storage
│   │   └── FolderService.java        # Folder business logic
│   └── JpApplication.java            # Main Spring Boot app
├── data/                             # H2 database files
├── uploads/                          # Uploaded files storage
├── start.sh                          # Quick start script
├── test-api.sh                       # API testing script
└── README.md                         # Full documentation
```

---

## 🚀 How to Run

### Option 1: Using the start script (Recommended)
```bash
./start.sh
```

### Option 2: Manual start
```bash
./mvnw spring-boot:run
```

---

## 🧪 Testing the API

Once the application is running, test it:

```bash
./test-api.sh
```

This will:
1. Create a root folder "Documents"
2. Get root contents
3. Create a subfolder "Photos"
4. Upload a test file
5. Show folder contents with the file

---

## 📡 API Endpoints

### **Folder Operations**

#### Create Folder
```bash
curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -d '{"name": "My Folder", "parentId": null}'
```

#### Get Root Folder Contents
```bash
curl http://localhost:8080/api/folders/root
```

#### Get Specific Folder Contents
```bash
curl http://localhost:8080/api/folders/{folderId}/contents
```

#### Delete Folder
```bash
curl -X DELETE http://localhost:8080/api/folders/{folderId}
```

### **File Operations**

#### Upload File
```bash
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@/path/to/your/file.pdf" \
  -F "folderId=1"
```

#### Download File
```bash
curl -O -J http://localhost:8080/api/files/{fileId}/download
```

#### Delete File
```bash
curl -X DELETE http://localhost:8080/api/files/{fileId}
```

---

## 🗄️ Database

### **Current Setup: H2 Database (No Installation Required!)**

- **Type:** File-based H2 database
- **Location:** `./data/gdrive_db.mv.db`
- **Console:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:./data/gdrive_db`
  - Username: `sa`
  - Password: (leave empty)

### **Tables Created Automatically:**
- `folders` - Stores folder metadata and hierarchy
- `file_items` - Stores file metadata (actual files in `uploads/` directory)

---

## 🔧 Configuration

### **Key Settings** (`application.properties`)

```properties
# Database (H2 - no installation needed)
spring.datasource.url=jdbc:h2:file:./data/gdrive_db

# File Upload
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB

# Uploads Directory
app.upload.dir=uploads
```

---

## 🎯 Architecture

**Clean Architecture Pattern:**
```
Client Request
    ↓
Controller (REST endpoints)
    ↓
Service (Business logic)
    ↓
Repository (Data access)
    ↓
Entity (Database tables)
```

---

## 💡 Key Technical Details

1. **User Management:** Currently uses hardcoded user ID: `"hardcoded-user-id"`
2. **File Storage:** Files stored physically in `uploads/` directory with UUID filenames
3. **Nested Folders:** Supports unlimited folder depth using parent-child relationships
4. **Cascade Delete:** Deleting a folder automatically deletes its contents
5. **Auto-generated IDs:** Database generates unique IDs for folders and files
6. **Timestamps:** Automatic `createdAt` and `updatedAt` tracking

---

## 📦 Technologies Used

- **Spring Boot 4.0.0** - Application framework
- **Spring Data JPA** - Database ORM
- **Spring Web** - REST API
- **H2 Database** - Embedded database
- **Hibernate** - JPA implementation
- **Lombok** - Reduce boilerplate code
- **Maven** - Build tool

---

## 🔮 Future Enhancements (v2)

- [ ] User authentication (Spring Security + JWT)
- [ ] File sharing and permissions
- [ ] File search functionality
- [ ] File versioning
- [ ] Trash/recycle bin
- [ ] File thumbnails for images
- [ ] Download folder as ZIP
- [ ] Cloud storage integration (AWS S3, Google Cloud Storage)
- [ ] React/Vue frontend
- [ ] Real-time notifications (WebSocket)

---

## 🐛 Troubleshooting

### Port 8080 already in use
```bash
# Kill the process using port 8080
lsof -ti:8080 | xargs kill -9
```

### Database locked error
```bash
# Remove the database lock file
rm data/gdrive_db.mv.db
```

### Clean start
```bash
# Clean build and restart
./mvnw clean install -DskipTests
./start.sh
```

---

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [H2 Database](https://www.h2database.com/)
- [REST API Best Practices](https://restfulapi.net/)

---

## ✨ Project Status

**Status:** ✅ **COMPLETE & READY TO USE**

All core features implemented and tested. The application is production-ready for single-user scenarios.

---

**Need help?** Check the README.md for detailed documentation or review the test-api.sh script for usage examples.

