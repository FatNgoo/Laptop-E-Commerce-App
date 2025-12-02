# OTech Build Script
# Run this to clean, build, and install the app

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  OTech Android App - Build & Install  " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Clean
Write-Host "[1/4] Cleaning previous build..." -ForegroundColor Yellow
.\gradlew clean
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Clean failed!" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Clean completed" -ForegroundColor Green
Write-Host ""

# Step 2: Build
Write-Host "[2/4] Building debug APK..." -ForegroundColor Yellow
.\gradlew assembleDebug
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Build failed!" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Build completed" -ForegroundColor Green
Write-Host ""

# Step 3: Check devices
Write-Host "[3/4] Checking connected devices..." -ForegroundColor Yellow
$devices = adb devices | Select-String -Pattern "device$"
if ($devices.Count -eq 0) {
    Write-Host "⚠️  No Android devices/emulators connected" -ForegroundColor Yellow
    Write-Host "Please connect a device or start an emulator" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "APK location:" -ForegroundColor Cyan
    Write-Host "app\build\outputs\apk\debug\app-debug.apk" -ForegroundColor White
    exit 0
}
Write-Host "✅ Device found" -ForegroundColor Green
Write-Host ""

# Step 4: Install
Write-Host "[4/4] Installing APK..." -ForegroundColor Yellow
.\gradlew installDebug
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Installation failed!" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Installation completed" -ForegroundColor Green
Write-Host ""

# Success
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  🎉 Build & Install Successful!       " -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Login credentials:" -ForegroundColor Cyan
Write-Host "  User:  username=user  password=user" -ForegroundColor White
Write-Host "  Admin: username=admin password=admin" -ForegroundColor White
Write-Host ""
Write-Host "APK location:" -ForegroundColor Cyan
Write-Host "app\build\outputs\apk\debug\app-debug.apk" -ForegroundColor White
