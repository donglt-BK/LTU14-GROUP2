# Đừng để tiền rơi (Olympia App)
## Danh sách API:
### Note: Cấu trúc một message:
* Mã ID của API: MessageType
* ID người gửi: int
* Content: Map (ContentType, Object)

STT | Tên | Tham số input | Đường dẫn URL | Tham số output | Đường xuất API | Loại thông điệp | Loại request
--- | --- | --- | --- | --- | --- | --- | ---
1 | Login | `USERNAME:String` `PASSWORD:String` | `/auth/login` | `USER_ID:int` | `/queue/auth/login` | `LOGIN` | `SEND`
_ | Sign up | `USERNAME:String` `PASSWORD:String` `NAME:String` `GENDER:int` | `/auth/sign_up` | `STATUS:boolean` | `/queue/auth/sign_up` | `SIGN_UP` | `SEND`
2 | Log out | `none` | `/user/signout` | Kết quả của Leave lobby hoặc của Game over
3 | Get user info | `none` | `/user/get-info` | `USER_ID:int` `USERNAME:String` `NAME:String` `GENDER:int` `BALANCE:int` | `/queue/user/get-info` | `GET_INFO`
4 | Change user info | **(Optional)** `NAME:String` `GENDER:int` | `/user/change-info` | _ | `/queue/user/change-info` | `CHANGE_INFO` | `SEND`
5 | Get recent history | `none` | `/user/get-recent-history` | `HISTORY_ROOM_ID:int[]` `HISTORY_CREATED_AT:Date[]` `HISTORY_ENDED_AT:Date[]` `HISTORY_RESULT_TYPE:int[]` *(thua -1, hòa 0, thắng 1)* `HISTORY_BALANCE_CHANGED:int[]` | `/queue/user/add-question` | `GET_RECENT_HISTORY` | `SEND`
6 | Add questions | `TOPIC_ID:int` `QUESTION:String` `ANSWER:String[]` `CORRECT_ANSWER_POS:int` | `/user/add-question` | `STATUS:true` | `none` | `ADD_QUESTION` | `SEND`
_ | Get all topics | `none` | `/user/get-all-topics` | `TOPIC_ID:int` `TOPIC_NAME:String` `TOPIC_DESCRIPTION:String` | `/queue/user/get-all-topics` | `GET_ALL_TOPICS` | `SEND`

	