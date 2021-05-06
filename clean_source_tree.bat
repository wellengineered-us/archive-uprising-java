@echo off

REM
REM Copyright ©2020 WellEngineered.us, all rights reserved.
REM Distributed under the MIT license: https://opensource.org/licenses/MIT
REM

CALL set_ps_env.bat

"%POWERSHELL_EXE_PATH%" -command .\clean_source_tree > "%temp%\clean_source_tree.log"
