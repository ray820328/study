一个复杂的mysql查询语句(case,when,then,left join )
select `a`.`id` AS `id`,`a`.`UserName` AS `UserName`,
(case when (`a`.`sRegDate` = _utf8'1990-01-01 00:00:00.0') then _utf8'' else cast(date_format(`a`.`sRegDate`,_utf8'%Y-%m-%d %H:%i:%S') as char charset utf8) end) AS `sRegDate`,(case when (`a`.`feeendtime` = _utf8'1990-01-01 00:00:00.0') then _utf8'' else cast(date_format(`a`.`feeendtime`,_utf8'%Y-%m-%d %H:%i:%S') as char charset utf8) end) AS `feeendtime`,
(case when (`a`.`iStatus` = _utf8'0') then _utf8'正常' when (`a`.`iStatus` = _utf8'1') then _utf8'锁定' when (`a`.`iStatus` = _utf8'2') then _utf8'欠费' when (`a`.`iStatus` = _utf8'') then _utf8'申请停机' else _utf8'' end) AS `iStatus`,
(case when (`a`.`PayType` = _utf8'0') then _utf8'免费' when (`a`.`PayType` = _utf8'2') then _utf8'小区网用户' when (`a`.`PayType` = _utf8'10') then _utf8'外部用户' else _utf8'' end) AS `PayType`,
`c`.`UserType` AS `sDh1`,`b`.`FeeName` AS `iFeeType`,
(case when (`a`.`PauseStartTime` = _utf8'1990-01-01 00:00:00.0') then _utf8'' else cast(date_format(`a`.`PauseStartTime`,_utf8'%Y-%m-%d %H:%i:%S') as char charset utf8) end) AS `PauseStartTime`,
(case when (`a`.`PauseStopTime` = _utf8'1990-01-01 00:00:00.0') then _utf8'' else cast(date_format(`a`.`PauseStopTime`,_utf8'%Y-%m-%d %H:%i:%S') as char charset utf8) end) AS `PauseStopTime`,
(case when (`a`.`AcctStartTime` = _utf8'1990-01-01 00:00:00.0') then _utf8'' else cast(date_format(`a`.`AcctStartTime`,_utf8'%Y-%m-%d %H:%i:%S') as char charset utf8) end) AS `AcctStartTime`,
(case when (`a`.`AcctStopTime` = _utf8'1990-01-01 00:00:00.0') then _utf8'' else cast(date_format(`a`.`AcctStopTime`,_utf8'%Y-%m-%d %H:%i:%S') as char charset utf8) end) AS `AcctStopTime`,
`a`.`speed` AS `speed`,
(case when (`a`.`idtype` = _utf8'7') then _utf8'身份证' when (`a`.`idtype` = _utf8'1') then _utf8'教职工' when (`a`.`idtype` = _utf8'2') then _utf8'本科生' when (`a`.`idtype` = _utf8'3') then _utf8'研究生' when (`a`.`idtype` = _utf8'4') then _utf8'离退生' when (`a`.`idtype` = _utf8'5') then _utf8'护照' when (`a`.`idtype` = _utf8'6') then _utf8'军官证' when (`a`.`idtype` = _utf8'8') then _utf8'其他' else _utf8'' end) AS `idtype`,
(case when (`a`.`iFeeTypeTime` = _utf8'1990-01-01 00:00:00.0') then _utf8'' else cast(date_format(`a`.`iFeeTypeTime`,_utf8'%Y-%m-%d %H:%i:%S') as char charset utf8) end) AS `iFeeTypeTime`,
`a`.`hth` AS `hth`
from ((`user_a` `a` left join `user_b` `b` on((`a`.`iFeeType` = `b`.`FeeCode`))) left join `user_c` `c` on((`c`.`TypeID` = `a`.`sDh1`)))
