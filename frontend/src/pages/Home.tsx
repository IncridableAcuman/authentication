import { useEffect } from "react";
import Navbar from "../components/main/Navbar"
import { useNavigate } from "react-router-dom";

const Home = () => {
  const navigate=useNavigate();

  useEffect(()=>{
    if(!localStorage.getItem("accessToken")){
      navigate("/auth");
    }
  },[navigate]);
  return (
    <div className="w-full min-h-screen bg-gray-200">
      <Navbar/>
      <h1 className="flex items-center justify-center mx-auto text-center pt-24 text-2xl font-extrabold text-green-900">Hi Developer</h1>
    </div>
  )
}

export default Home