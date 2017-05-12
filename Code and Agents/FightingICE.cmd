::
setlocal ENABLEDELAYEDEXPANSION
set PATH=%PATH%;./lib/native/windows
FOR /L %%I IN (1,1,1000) DO (
java -cp FightingICE.jar;./lib/lwjgl.jar;./lib/lwjgl_util.jar;./lib/gameLib.jar;./lib/fileLib.jar;./lib/jinput.jar;./lib/commons_csv.jar;./lib/javatuples-1.2.jar;./lib/py4j0.10.4.jar Main -n 1 --c1 ZEN --c2 ZEN --a1 Prototype2 --a2 RandomAI --fastmode --disable-window)
endlocal
exit
:: 