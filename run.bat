@echo off

chcp 1251 > nul

echo JAVA_HOME=%JAVA_HOME%
java -version
echo.

set /p input_path=������� ���� � PDF-�����: 
set /p export_type=������� ������ ��� ����������� (csv, xlsx): 

call gradlew run -S -PinputPath=%input_path% -PexportType=%export_type%

pause