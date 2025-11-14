import toast from "react-hot-toast";
import axiosInstance from "../../api";
import { useNavigate } from "react-router-dom";
import { useState } from "react";

const Navbar = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const handleSubmit = async () => {
    try {
      setLoading(true);

      const { data } = await axiosInstance.post("/auth/logout");

      if (data) {
        localStorage.clear();
        toast.success("Logged out successfully");
        navigate("/auth");
      }

    } catch (error) {
      console.log(error);
      toast.error("Failed to log out");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex items-center justify-between py-4 px-4 sm:px-6 md:px-8 lg:px-10 bg-white shadow">
      <h1 className="text-lg font-semibold text-green-700">Auth</h1>

      {loading ? (
        <button
          type="button"
          disabled
          className="bg-green-400 text-white px-5 py-2 rounded-md shadow flex items-center gap-2"
        >
          <svg
            className="size-5 animate-spin"
            viewBox="0 0 24 24"
            fill="none"
          >
            <circle
              className="opacity-25"
              cx="12"
              cy="12"
              r="10"
              stroke="currentColor"
              strokeWidth="4"
            ></circle>
            <path
              className="opacity-75"
              fill="currentColor"
              d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z"
            ></path>
          </svg>
          Loading...
        </button>
      ) : (
        <button
          onClick={handleSubmit}
          className="bg-green-500 text-white px-5 py-2 rounded-md shadow hover:bg-green-400 transition"
        >
          Sign Out
        </button>
      )}
    </div>
  );
};

export default Navbar;
