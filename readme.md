# LTU14 - Group 10 - Olympia Client App readme file

## Thuật ngữ

- Người chơi riêng lẻ: Người chơi tìm trận.
- Trận đấu (Game)
- Hiệp đấu (Set)

## Danh sách các màn hình và chức năng:

* Màn hình đăng nhập: có thể dùng để đăng nhập và đăng ký tài khoản.
* Màn hình chính: sau khi đăng nhập, màn hình này sẽ hiện lên. Người chơi có thể chọn một trong các hành động sau:
    * Tìm người chơi: hệ thống sẽ tự tìm kiếm những người riêng lẻ.
    * Mời người chơi: người chơi có thể mời một người chơi khác chơi cùng bằng cách tìm theo tiêu chí (id hoặc tên) của họ.
* Màn hình phòng đợi (lobby): sau khi tìm được người chơi khác, người chơi sẽ được chuyển đến màn hình này. Ở đây có các chức năng sau:
    * Đặt cược: người chơi sẽ đặt cược một khoản tiền theo hạn mức mà trò chơi đưa ra (100, 500, 1000) và hạn mức lớn nhất sẽ tuỳ thuộc vào số tiền mà cả hai bên có.
    * Thoát khỏi phòng: khi một trong hai hoặc cả hai người rời phòng, phòng sẽ bị xoá; số tiền cược sẽ không bị trừ nếu người chơi thoát khỏi phòng trước khi trò chơi bắt đầu.
    * Bắt đầu trò chơi: trò chơi sẽ bắt đầu khi cả hai người cùng bấm nút _Sẵn sàng_.
 
## Gameplay

* Trò chơi có thể thức tương tự chương trình "Đừng để tiền rơi".
* Mỗi trận đấu sẽ có 10 chủ đề, tương ứng mỗi chủ đề là 1 câu hỏi.
* 1 câu hỏi có 4 phương án và 1 đáp án đúng.
* Mỗi câu hỏi được trò chơi chọn ngẫu nhiên trong ngân hàng câu hỏi tương ứng với chủ đề.
* Mỗi hiệp đấu, người chơi trả lời một câu hỏi trong vòng 30 giây, bằng cách đặt cược tiền vào *những* phương án mà mình cho là đúng.
* Có thể đặt tiền vào tối đa 3 phương án và phải sử dụng hết số tiền đã cược ban đầu.
* Cuối mỗi hiệp đấu, chỉ có số tiền được đặt cược trên phương án đúng mới được giữ lại.
* Trò chơi sẽ kết thúc khi một trong hai người chơi hết tiền.
* Nếu sau 10 hiệp mà cả 2 người chơi vẫn còn tiền, hệ thống sẽ xét người chiến thắng dựa trên số tiền còn lại của từng người chơi. Nếu cả 2 người chơi có số tiền bằng nhau, trận đấu sẽ kết thúc với kết quả hoà. Cả 2 người chơi đều có thể giữ lại số tiền cược ban đầu. Luật áp dụng tương tự trong mỗi hiệp đấu.

## Hướng dẫn cài đặt

### JavaFX scene builder
* Vào trang sau để download binary:
[Trang download](https://www.oracle.com/java/technologies/javafxscenebuilder-1x-archive-downloads.html)
* Chọn *javafx_scenebuilder-2_0-windows.msi* để tải (cần phải đăng nhập để tải)
* Cài đặt *JavaFX Scene Builder* theo hướng dẫn của installer.
* Vào *Intellij*, chọn **Settings/Preferences** (`Ctrl+Alt+S`), chọn **JavaFX** trong phần **Languages and Frameworks**.
* Chọn đường dẫn đến file thực thi của *JavaFX Scene Builder* đã cài đặt và nhấn **OK**.
