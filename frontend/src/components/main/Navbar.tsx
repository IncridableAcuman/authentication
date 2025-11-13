import toast from "react-hot-toast";
import axiosInstance from "../../api";
import { useNavigate } from "react-router-dom";


const Navbar = () => {
  const navigate=useNavigate();
  const handleSubmit = async ()=>{
    try {
     const {data} = await axiosInstance.post("/auth/logout");
     if(data){
      localStorage.clear();
      toast.success("Successfully");
      navigate("/auth");
     }

    } catch (error) {
      console.log(error);
      toast.error("Failed with logged out");
    }
  }

  return (
    <div className="flex items-center justify-between py-4 px-4 sm:px-6 md:px-8 lg:px-10 bg-white shadow">
      <h1 className="text-lg font-semibold text-green-700">Auth</h1>
      <button onClick={handleSubmit} className="bg-green-500 text-white px-5 py-2 rounded-md shadow cursor-pointer hover:bg-green-400 transition duration-300">Sign Out</button>
    </div>
  )
}

export default Navbar