@echo off
REM open_terminal_here.bat — opens a cmd.exe terminal in this folder.
REM Usage: double-click this file or run it from Explorer.
:: Change to the script directory
cd /d "%~dp0"
:: Open cmd and keep it open
cmd.exe /K
