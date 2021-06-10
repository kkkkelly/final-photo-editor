
1.在本資料夾的空白處按Shift+滑鼠右鍵
2.選擇「在這裡開啟PowerShell視窗」
3.輸入「Java --module-path $env:PATH_TO_FX --add-modules=javafx.controls,javafx.fxml,javafx.swing PhotoEditorMain」

<java>
※版本: openjdk-11.0.2
※環境變數:
(在Path中加入)
變數名稱 : Path
變數值 : C:\Program Files\Java\jdk-11.0.2\bin 

(新增)
變數名稱 : CLASSPATH
變數值 : .; 

<javafx>
※版本: openjfx-11.0.2
※環境變數:
(新增)
變數名稱 : PATH_TO_FX
變數值 : C:\Program Files\Java\javafx-sdk-11.0.2\lib
