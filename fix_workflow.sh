sed -i 's/echo "GEMINI_API_KEY=MY_GEMINI_API_KEY" > .env/echo "GEMINI_API_KEY=${{ secrets.GEMINI_API_KEY }}" > .env/g' .github/workflows/android.yml
