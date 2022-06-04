# Java Final Program

## 事前準備

需要安裝weka與commons-math才能順利執行

+ [Weka 3-8-6 下載](https://prdownloads.sourceforge.net/weka/weka-3-8-6.zip)

+ [commons-math 3.6.1 下載](https://dlcdn.apache.org//commons/math/binaries/commons-math3-3.6.1-bin.zip)

另外提供範例CSV檔案(在\src\application\\*.csv)

+ 主畫面點Linear Model    &rarr;   點Choose file  &rarr; 選擇 mtcars.csv 

+ 主畫面點Clustering      &rarr;   點Choose file  &rarr; 選擇 iris.2D.arff

+ 主畫面點Introduction    &rarr;   選擇two-sample t-test 點confirm  &rarr;   點Choose file  &rarr; 選擇 ttest_reject.csv/ttest_noreject.csv

+ 主畫面點Introduction    &rarr;   選擇pair t-test 點confirm        &rarr;   點Choose file  &rarr; 選擇 pair_reject.csv/pair_noreject.csv

## Eclipse配置設定

在Eclipse中匯入專案的方式如下

> File &rarr; Open Projects from File System &rarr; Directory(資料夾)/Archive(壓縮檔) &rarr; Finish

如果上述方式無法成功匯入，可以嘗試進行以下操作

> File &rarr; Open Projects from File System &rarr; Show other specialized import wizards超連結 &rarr;
> 
> General下的Existing Projects into Workspace &rarr; Browse &rarr; Finish

最後到Project的Build Path中，更改weka與commons-math的原始路徑，設定為本地端的新路徑
