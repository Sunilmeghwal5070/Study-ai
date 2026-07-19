with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "r") as f:
    lines = f.readlines()

out = []
skip = False
for line in lines:
    if "val tts = remember(context)" in line:
        skip = True
    
    if skip:
        if "val snackbarHostState" in line:
            skip = False
            out.append("    DisposableEffect(Unit) { onDispose { com.example.ui.utils.MediaUtils.stopSpeaking() } }\n")
            out.append(line)
    else:
        out.append(line)

with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "w") as f:
    f.writelines(out)
