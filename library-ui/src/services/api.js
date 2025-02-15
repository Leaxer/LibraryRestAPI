import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api";

// User
export const getUsers = () => axios.get(`${API_BASE_URL}/users`);
export const addUser = (user) => axios.post(`${API_BASE_URL}/users`, user);
export const deleteUser = (id) => axios.delete(`${API_BASE_URL}/users/${id}`);

// Book
export const getBooks = () => axios.get(`${API_BASE_URL}/books`);
export const addBook = (book) => axios.post(`${API_BASE_URL}/books`, book);
export const deleteBook = (id) => axios.delete(`${API_BASE_URL}/books/${id}`);

// Borrow book
export const borrowBook = (userId, bookId) =>
    axios.post(`${API_BASE_URL}/users/${userId}/borrow/${bookId}`);

// Return book
export const returnBook = (userId, bookId) =>
    axios.post(`${API_BASE_URL}/users/${userId}/return/${bookId}`);
