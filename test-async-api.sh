#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Testing Async File Upload API${NC}"
echo -e "${BLUE}========================================${NC}"

BASE_URL="http://localhost:8080/api/async/files"

# Test 1: Create a test file
echo -e "\n${GREEN}1. Creating test files...${NC}"
echo "This is test file 1" > /tmp/async-test-1.txt
echo "This is test file 2" > /tmp/async-test-2.txt
echo "This is test file 3" > /tmp/async-test-3.txt
echo "Files created"

# Test 2: Async upload (returns immediately with task ID)
echo -e "\n${GREEN}2. Uploading file asynchronously...${NC}"
RESPONSE=$(curl -s -X POST "$BASE_URL/upload" \
  -F "file=@/tmp/async-test-1.txt" \
  -F "folderPath=")

echo "$RESPONSE" | jq '.'
TASK_ID=$(echo "$RESPONSE" | jq -r '.taskId')
echo -e "${YELLOW}Task ID: $TASK_ID${NC}"

# Test 3: Check upload status
echo -e "\n${GREEN}3. Checking upload status (wait 2 seconds)...${NC}"
sleep 2
curl -s -X GET "$BASE_URL/status/$TASK_ID" | jq '.'

# Test 4: Upload file with sync wait (waits for completion)
echo -e "\n${GREEN}4. Uploading file with sync wait...${NC}"
curl -s -X POST "$BASE_URL/upload-sync" \
  -F "file=@/tmp/async-test-2.txt" \
  -F "folderPath=" | jq '.'

# Test 5: Batch upload multiple files
echo -e "\n${GREEN}5. Batch uploading multiple files...${NC}"
BATCH_RESPONSE=$(curl -s -X POST "$BASE_URL/batch-upload" \
  -F "files=@/tmp/async-test-1.txt" \
  -F "files=@/tmp/async-test-2.txt" \
  -F "files=@/tmp/async-test-3.txt" \
  -F "folderPath=")

echo "$BATCH_RESPONSE" | jq '.'

# Get task IDs from batch response
TASK_ID_1=$(echo "$BATCH_RESPONSE" | jq -r '.taskIds["async-test-1.txt"]')
TASK_ID_2=$(echo "$BATCH_RESPONSE" | jq -r '.taskIds["async-test-2.txt"]')
TASK_ID_3=$(echo "$BATCH_RESPONSE" | jq -r '.taskIds["async-test-3.txt"]')

# Test 6: Check status of all batch uploads
echo -e "\n${GREEN}6. Checking batch upload status (wait 3 seconds)...${NC}"
sleep 3

echo -e "\n${YELLOW}Task 1 Status:${NC}"
curl -s -X GET "$BASE_URL/status/$TASK_ID_1" | jq '.'

echo -e "\n${YELLOW}Task 2 Status:${NC}"
curl -s -X GET "$BASE_URL/status/$TASK_ID_2" | jq '.'

echo -e "\n${YELLOW}Task 3 Status:${NC}"
curl -s -X GET "$BASE_URL/status/$TASK_ID_3" | jq '.'

# Test 7: Delete a task
echo -e "\n${GREEN}7. Deleting completed task...${NC}"
curl -s -X DELETE "$BASE_URL/status/$TASK_ID_1"
echo -e "Task $TASK_ID_1 deleted"

# Test 8: Verify deletion
echo -e "\n${GREEN}8. Verifying task deletion...${NC}"
curl -s -X GET "$BASE_URL/status/$TASK_ID_1"
echo ""

echo -e "\n${BLUE}========================================${NC}"
echo -e "${BLUE}All async tests completed!${NC}"
echo -e "${BLUE}========================================${NC}"
echo -e "\nCheck the ${GREEN}storage/${NC} directory to see uploaded files!"
