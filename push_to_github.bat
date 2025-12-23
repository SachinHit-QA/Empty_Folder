@echo off
REM push_to_github.bat - Create remote (optional) and push current project to GitHub.
REM Usage: push_to_github.bat [remote-or-owner/repo] [public|private]
REM Examples:
REM   push_to_github.bat               -> pushes to default https://github.com/SachinHit-QA/Empty_Folder.git
REM   push_to_github.bat myuser/MyRepo  -> creates https://github.com/myuser/MyRepo.git and pushes
REM   push_to_github.bat https://github.com/myuser/MyRepo.git

:: Default remote URL (provided by the user)
set "DEFAULT_REMOTE=https://github.com/SachinHit-QA/Empty_Folder.git"

:: Current folder name (used for commit message when arg not provided)
for %%I in ("%cd%") do set "CURDIR=%%~nxi"

:: Parse args
set "ARG=%~1"
set "VISIBILITY=%~2"
if "%VISIBILITY%"=="" set "VISIBILITY=public"

:: Determine remote URL to use
set "REMOTE_URL="
if not "%ARG%"=="" (
    echo Received argument: %ARG%
    echo Checking argument format...
    echo %ARG% | findstr /i "://" >nul 2>&1
    if not errorlevel 1 (
        set "REMOTE_URL=%ARG%"
    ) else (
        echo %ARG% | findstr /i "@" >nul 2>&1
        if not errorlevel 1 (
            set "REMOTE_URL=%ARG%"
        ) else (
            rem treat as owner/repo
            set "REMOTE_URL=https://github.com/%ARG%.git"
        )
    )
) else (
    set "REMOTE_URL=%DEFAULT_REMOTE%"
)

echo Using remote: %REMOTE_URL%

:: Ensure Git is available
git --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Git is not installed or not on PATH. Install Git and re-run this script.
    pause
    exit /b 1
)

:: Detect GitHub CLI (optional)
gh --version >nul 2>&1
if not errorlevel 1 (
    set "HAS_GH=1"
) else (
    set "HAS_GH=0"
)

:: Initialize repo if not already a git repo
git rev-parse --is-inside-work-tree >nul 2>&1
if errorlevel 1 (
    echo Initializing new git repository...
    git init
    git checkout -b main
) else (
    echo Found existing git repository.
)

:: Stage & commit (if any changes)
echo Staging files...
git add .
set "COMMIT_MSG=Initial commit: %CURDIR%"
if not "%ARG%"=="" set "COMMIT_MSG=Initial commit: %ARG%"
echo Committing (message: %COMMIT_MSG%)...
git commit -m "%COMMIT_MSG%" >nul 2>&1
if errorlevel 1 (
    echo (Nothing to commit or commit failed — continuing)
)

:: Configure origin remote
git remote get-url origin >nul 2>&1
if errorlevel 1 (
    echo Adding remote origin -> %REMOTE_URL%
    git remote add origin %REMOTE_URL%
) else (
    echo Remote 'origin' already exists; setting URL -> %REMOTE_URL%
    git remote set-url origin %REMOTE_URL%
)

:: Ensure current branch is 'main'
for /f "delims=" %%B in ('git rev-parse --abbrev-ref HEAD 2^>nul') do set "CURRENT_BRANCH=%%B"
if not defined CURRENT_BRANCH (
    echo Creating and switching to branch 'main'...
    git checkout -b main
) else (
    if "%CURRENT_BRANCH%"=="main" (
        echo Already on branch 'main'.
    ) else (
        echo Renaming current branch to 'main' (or creating it)...
        git branch -M main
    )
)

:: Try to push
echo Pushing to origin main...
git push -u origin main
if not errorlevel 1 (
    echo Push succeeded.
    goto :verify
) else (
    echo Push failed. Possible causes: authentication, missing remote repo, branch protection.
    if "%HAS_GH%"=="1" (
        echo Attempting to create repo with 'gh' and push...
        rem derive owner/repo for gh if ARG was provided or parse from REMOTE_URL
        set "GH_REPO=%ARG%"
        if "%GH_REPO%"=="" (
            set "TMP=%REMOTE_URL%"
            set "TMP=%TMP:https://=%"
            set "TMP=%TMP:http://=%"
            set "TMP=%TMP:git@=%"
            set "TMP=%TMP:github.com/=%"
            set "TMP=%TMP:.git=%"
            set "GH_REPO=%TMP%"
        )
        echo gh repo create %GH_REPO% --%VISIBILITY% --source . --remote origin --push
        gh repo create %GH_REPO% --%VISIBILITY% --source . --remote origin --push
        if errorlevel 1 (
            echo 'gh' failed to create or push. Please run 'gh auth login' or create the repo manually on GitHub.
            goto :manualFallback
        ) else (
            echo Successfully created and pushed using gh.
            goto :verify
        )
    ) else (
        goto :manualFallback
    )
)

:manualFallback
echo.
echo MANUAL ACTION REQUIRED: If the remote repo does not exist, create it on GitHub at:
echo   %REMOTE_URL%
echo Then run these commands to push:
echo   git remote add origin %REMOTE_URL%
echo   git branch -M main
echo   git push -u origin main
echo.
echo To let this script create the repo automatically, install GitHub CLI (https://cli.github.com/) and authenticate (gh auth login), then re-run this script.
pause
goto :eof

:verify
echo.
echo Verifying remote configuration...
git remote -v
echo Done.
pause
goto :eof