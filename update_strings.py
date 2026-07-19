import re

with open("app/src/main/java/com/example/ui/utils/Strings.kt", "r") as f:
    content = f.read()

# EnglishStrings
english_new_subjects = """    "gk" to "GK",
    "hindi_literature" to "Hindi Literature",
    "english_literature" to "English Literature",
    "sanskrit" to "Sanskrit",
    "political_science" to "Political Science",
    "hindi_subj" to "Hindi",
    "english_subj" to "English",
"""
content = content.replace('    "gk" to "GK",\n', english_new_subjects)

# HindiStrings
hindi_new_subjects = """    "gk" to "सामान्य ज्ञान",
    "hindi_literature" to "हिंदी साहित्य",
    "english_literature" to "अंग्रेज़ी साहित्य",
    "sanskrit" to "संस्कृत",
    "political_science" to "राजनीति विज्ञान",
    "hindi_subj" to "हिंदी",
    "english_subj" to "अंग्रेजी",
"""
content = content.replace('    "gk" to "सामान्य ज्ञान",\n', hindi_new_subjects)

with open("app/src/main/java/com/example/ui/utils/Strings.kt", "w") as f:
    f.write(content)
