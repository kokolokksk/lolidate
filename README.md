# lolidate
和萝莉约会（不是

# 需求
- 应用版本检测
- 应用版本更新
# 实现
服务器存放2个版本（以上）文件 例如1.0.1,1.0.2;每个版本文件夹包含
一个文件列表和hash的记录。
服务器需要定时对没有记录的版本生成文件
在更新过程中，client 首先查询更新信息，如存在新版本，则由用户决定是否更新。
确定更新后，向服务器查询是否存在，例如patch-101-102.7z的升级文件，存在则
下载更新，反之：
 - 两个版本（举例client 1.0.1，服务器1.0.2）是否存在与服务器，存在，服务器应定时生成patch-101-102.7z,如果无，此刻应主动生成并返回client文件。
 - 服务器只存在1.0.2,1.0.3，client为1.0.1 ，则对比1.0.3中的文件记录 与 client 文件记录，并生成新的patch文件（并保存到patch文件 下次可直接下载到）


功能
- 定时检测版本文件夹，对没有file.list文件的进行生成。
- 通过两个file.list对比生成patch文件，patch包含对应文件、位置的记录文件和当前版本的file.list文件
- 对外提供差异版本patch下载