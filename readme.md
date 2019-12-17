# Đừng để tiền rơi (Olympia App)

## Thành viên trong nhóm

STT | Họ tên | MSSV
--- | --- | ---
1 | Lương Tiến Đồng | 20158100
2 | Trương Minh Đức | 20158109
3 | Nguyễn Dương Khoa | 20138975
4 | Nguyễn Việt An | 20158002

## Triển khai

### Giới thiệu công nghệ

Dự án này được xây dựng theo mô hình Client-Server với:

- Server xây dựng trên [Java Spring](https://spring.io/) ![](./img/spring.png)
- Client xây dựng trên JavaFx [JavaFx]() ![](./img/javafx.png)
- Cơ sở dữ liệu sử dụng [MySQL](https://www.mysql.com/) ![](./img/mysql.png)

### Cài đặt công cụ

- Cài đặt IDE cho Java: [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
- Cài đặt [JavaFx SceneBuilder](https://www.oracle.com/java/technologies/javafxscenebuilder-1x-archive-downloads.html)
- Cài đặt [MySQL Community Server](https://dev.mysql.com/downloads/mysql/) và [MySQL Workbench](https://dev.mysql.com/downloads/workbench/)

### Thiết lập môi trường

Sau khi cài đặt các công cụ như trên. Clone repo này ra hai folder khác nhau, một folder clone nhánh server, folder còn lại clone nhánh client.

#### Đối với Server

Mở folder server bằng IntelliJ, sửa thông số sau trong file `application.yml` theo thiết lập khi cài đặt cơ sở dữ liệu.

```
username: root
password: 12345678
```

Xong xuôi, nhấn nút Run để chạy Server.

#### Đối với Client

Mở folder client bằng IntelliJ. Với những file có dịnh dạng `fxml`, chuột phải chọn mở trong SceneBuilder để thao tác được trên giao diện.

## Một số thông tin hữu ích

- [Gameplay](./note/gameplay.md)
- [Danh sách API](./note/api.md)
- [Hướng dẫn cài đặt SceneBuilder](./note/scenebuilder.setup)