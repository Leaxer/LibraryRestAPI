import { Link } from "react-router-dom";

const Navbar = () => {
    return (
        <nav className="bg-blue-600 p-4 text-white flex justify-between">
            <h1 className="text-xl font-bold">Library System</h1>
            <div>
                <Link to="/users" className="mx-2">Users</Link>
                <Link to="/books" className="mx-2">Books</Link>
            </div>
        </nav>
    );
};

export default Navbar;
