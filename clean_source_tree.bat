@echo off

REM
REM Copyright ©2017-2019 SyncPrem, all rights reserved.
REM Distributed under the MIT license: https://opensource.org/licenses/MIT
REM

CALL set-ps-env.bat

"%POWERSHELL_CORE_EXE_PATH%\pwsh.exe" -command .\clean_source_tree > "%temp%\clean_source_tree.log"
