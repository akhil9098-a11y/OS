import tkinter as tk
from tkinter import messagebox
import pandas as pd

from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.naive_bayes import MultinomialNB
from sklearn.neighbors import NearestNeighbors
from difflib import get_close_matches

# -------- LOAD DATA --------
try:
    df = pd.read_csv("books.csv")
except Exception as e:
    print("CSV ERROR:", e)
    input("Press Enter to exit...")
    exit()

# Combine features
df["content"] = df["title"] + " " + df["description"]

# -------- TF-IDF --------
vectorizer = TfidfVectorizer(stop_words='english')
X = vectorizer.fit_transform(df["content"])

# -------- NAIVE BAYES --------
nb = MultinomialNB()
nb.fit(X, df["genre"])

# -------- GLOBAL KNN --------
knn_global = NearestNeighbors(metric='cosine')
knn_global.fit(X)

# -------- FIND CLOSE MATCH --------
def find_book(title):
    titles = df["title"].tolist()
    match = get_close_matches(title, titles, n=1, cutoff=0.4)
    return match[0] if match else None

# -------- RECOMMEND FUNCTION --------
def recommend():
    try:
        user_input = entry.get().strip()

        if not user_input:
            messagebox.showwarning("Warning", "Enter book name")
            return

        match = find_book(user_input)

        if not match:
            messagebox.showerror("Error", "Book not found in dataset")
            return

        idx = df[df["title"] == match].index[0]

        # Predict genre
        input_vec = vectorizer.transform([df.iloc[idx]["content"]])
        predicted_genre = nb.predict(input_vec)[0]

        # Filter by genre
        genre_df = df[df["genre"] == predicted_genre]

        if len(genre_df) < 3:
            genre_df = df

        genre_X = vectorizer.transform(genre_df["content"])

        k = min(5, len(genre_df))
        knn_local = NearestNeighbors(n_neighbors=k, metric='cosine')
        knn_local.fit(genre_X)

        local_idx = genre_df.index.get_loc(idx)

        distances, indices = knn_local.kneighbors(genre_X[local_idx])

        result = f"Book Found: {match}\n"
        result += f"Genre: {predicted_genre}\n\n"
        result += "Recommendations:\n\n"

        for i in indices[0]:
            book = genre_df.iloc[i]
            if book["title"] != match:
                result += f"- {book['title']} by {book['author']}\n"

        show_result(result)

    except Exception as e:
        messagebox.showerror("Error", str(e))


# -------- RESULT WINDOW --------
def show_result(text):
    win = tk.Toplevel(root)
    win.title("Recommendations")
    win.geometry("550x420")
    win.configure(bg="#1e1e2f")

    tk.Label(win,
             text="Your Recommendations",
             font=("Segoe UI", 16, "bold"),
             bg="#1e1e2f",
             fg="#00d4ff").pack(pady=10)

    txt = tk.Text(win,
                  wrap="word",
                  font=("Segoe UI", 11),
                  bg="#2a2a40",
                  fg="white",
                  insertbackground="white",
                  bd=0)
    txt.pack(expand=True, fill="both", padx=15, pady=10)

    txt.insert("1.0", text)
    txt.config(state="disabled")


# -------- MAIN GUI --------
root = tk.Tk()
root.title("BookBuddy")
root.geometry("550x380")
root.configure(bg="#1e1e2f")

tk.Label(root,
         text="BookBuddy",
         font=("Segoe UI", 22, "bold"),
         bg="#1e1e2f",
         fg="white").pack(pady=15)

tk.Label(root,
         text="Smart Book Recommendation System",
         font=("Segoe UI", 11),
         bg="#1e1e2f",
         fg="#aaaaaa").pack()

entry = tk.Entry(root,
                 width=35,
                 font=("Segoe UI", 12),
                 bg="#2a2a40",
                 fg="white",
                 insertbackground="white",
                 bd=0)
entry.pack(pady=25, ipady=6)

tk.Button(root,
          text="Recommend",
          command=recommend,
          bg="#00d4ff",
          fg="black",
          font=("Segoe UI", 12, "bold"),
          activebackground="#00aacc",
          padx=15,
          pady=6,
          bd=0).pack(pady=10)

tk.Button(root,
          text="Exit",
          command=root.quit,
          bg="#ff4d4d",
          fg="white",
          font=("Segoe UI", 10),
          bd=0).pack(pady=10)

root.mainloop()