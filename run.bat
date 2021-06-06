@echo off
chcp 1251 > nul
set /p input_path=¬ведите путь к PDF-файлу: 
set /p export_type=¬ведите формат дл€ конвертации (csv, xlsx): 
call gradlew run -S -PinputPath=%input_path% -PexportType=%export_type%
pause