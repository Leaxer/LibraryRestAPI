import { useEffect, useState } from "react";
import { getBooks, addBook, deleteBook } from "../services/api";

const Books = () => {
    const [books, setBooks] = useState([]);
    const [title, setTitle] = useState("");
    const [author, setAuthor] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        fetchBooks();
    }, []);

    const fetchBooks = async () => {
        const res = await getBooks();
        setBooks(res.data);
    };

    const handleAddBook = async () => {
        if (title.trim() && author.trim()) {
            await addBook({ title, author });
            fetchBooks();
            setTitle("");
            setAuthor("");
        }
    };

    const handleDeleteBook = async (id) => {
        try {
            await deleteBook(id);
            fetchBooks();
        } catch (error) {
            setErrorMessage(error.response.data);
        }
    };

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-2xl font-bold mb-4">Books</h1>

            <div className="mb-4 flex gap-2">
                <input
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    placeholder="Title"
                    className="border p-2"
                />
                <input
                    value={author}
                    onChange={(e) => setAuthor(e.target.value)}
                    placeholder="Author"
                    className="border p-2"
                />
                <button onClick={handleAddBook} className="bg-green-500 text-white p-2">Add Book</button>
            </div>

            {errorMessage && (
                <div className="text-red-500 mb-4">{errorMessage}</div>
            )}

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {books.map((book) => (
                    <div key={book.id} className="p-4 border rounded-lg shadow-md">
                        <h2 className="text-lg font-bold">{book.title}</h2>
                        <p>Author: {book.author}</p>
                        <p>{book.borrowed ? "Borrowed" : "Available"}</p>

                        <button
                            onClick={() => handleDeleteBook(book.id)}
                            className="mt-2 bg-red-500 text-white p-2"
                        >
                            Delete Book
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Books;
