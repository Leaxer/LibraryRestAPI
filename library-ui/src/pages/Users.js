import { useEffect, useState } from "react";
import { getUsers, addUser, deleteUser, borrowBook, returnBook } from "../services/api";

const Users = () => {
  const [users, setUsers] = useState([]);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [bookId, setBookId] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const res = await getUsers();
      setUsers(res.data);
    } catch (error) {
      setErrorMessage("Kullanıcılar yüklenirken hata oluştu.");
    }
  };

  const handleAddUser = async () => {
    if (name.trim() && email.trim()) {
      try {
        await addUser({ name, email });
        fetchUsers();
        setName("");
        setEmail("");
        setErrorMessage("");
      } catch (error) {
        setErrorMessage("Kullanıcı eklenirken hata oluştu.");
      }
    }
  };

  const handleDeleteUser = async (id) => {
    try {
      await deleteUser(id);
      fetchUsers();
      setErrorMessage("");
    } catch (error) {
      setErrorMessage("Kullanıcı silinirken hata oluştu.");
    }
  };

  const handleBorrowBook = async (userId) => {
    if (!bookId.trim()) {
      setErrorMessage("Lütfen bir kitap ID'si girin.");
      return;
    }
    try {
      await borrowBook(userId, bookId);
      fetchUsers();
      setBookId("");
      setErrorMessage("");
    } catch (error) {
      setErrorMessage(error.response?.data || "Kitap ödünç alınırken hata oluştu.");
    }
  };

  const handleReturnBook = async (userId, bookId) => {
    try {
      await returnBook(userId, bookId);
      fetchUsers();
      setErrorMessage("");
    } catch (error) {
      if (error.response && error.response.data) {
        setErrorMessage(JSON.stringify(error.response.data));
      } else {
        setErrorMessage("Kitap iade edilirken hata oluştu.");
      }
    }
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Users</h1>

      {/* Kullanıcı Ekleme */}
      <div className="mb-4 flex gap-2">
        <input
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Name"
          className="border p-2"
        />
        <input
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="Email"
          className="border p-2"
        />
        <button onClick={handleAddUser} className="bg-green-500 text-white p-2">
          Add User
        </button>
      </div>

      {/* Hata Mesajı */}
      {errorMessage && (
        <div className="text-red-500 mb-4">{errorMessage}</div>
      )}

      {/* Kullanıcı Listesi */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {users.map((user) => (
          <div key={user.id} className="p-4 border rounded-lg shadow-md">
            <h2 className="text-lg font-bold">{user.name}</h2>
            <p>Email: {user.email}</p>

            {/* Ödünç Kitap Alma */}
            <div className="mt-2">
              <input
                value={bookId}
                onChange={(e) => setBookId(e.target.value)}
                placeholder="Book ID"
                className="border p-2 w-20"
              />
              <button
                onClick={() => handleBorrowBook(user.id)}
                className="bg-blue-500 text-white p-2 ml-2"
              >
                Borrow
              </button>
            </div>

            {/* Ödünç Alınan Kitaplar */}
            <h3 className="mt-2 font-semibold">Borrowed Books:</h3>
            <ul className="list-disc ml-4">
              {user.borrowedBooks && user.borrowedBooks.length > 0 ? (
                user.borrowedBooks.map((book) => {
                  console.log("Book ID:", book.id); // 📌 Book ID'yi KONTROL ET!
                  return (
                    <li key={book.id} className="text-sm">
                      <strong>{book.title}</strong> by {book.author} <br />
                      <span className="text-red-500">Due: {book.dueDate}</span>
                      <button
                        onClick={() => handleReturnBook(user.id, book.id)}
                        className="ml-2 bg-orange-500 text-white p-1 text-xs"
                      >
                        Return
                      </button>
                    </li>
                  );
                })
              ) : (
                <p>No borrowed books.</p>
              )}
            </ul>

            {/* Kullanıcıyı Silme */}
            <button
              onClick={() => handleDeleteUser(user.id)}
              className="mt-2 bg-red-500 text-white p-2"
            >
              Delete User
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Users;
