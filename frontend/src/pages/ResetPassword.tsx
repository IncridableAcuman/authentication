import { Eye, EyeClosed, Lock } from "lucide-react";
import { useState } from "react";
import toast from "react-hot-toast";
import { useLocation, useNavigate } from "react-router-dom";
import axiosInstance from "../api";

const ResetPassword = () => {
    const [password,setPassword]=useState('');
    const [confirmPassword,setConfirmPassword]=useState('');
    const location=useLocation();
    const queryParams=new URLSearchParams(location.search);
    const token = queryParams.get("token");
    const [showPassword,setShowPassword]=useState(false);
    const navigate=useNavigate();

    console.log(token);

    const handleSubmit = async (e:React.FormEvent<HTMLFormElement>) =>{
      e.preventDefault();
      if(password!==confirmPassword){
        toast.error("Password must be equal");
        return;
      }
      try {
        const {data} = await axiosInstance.put("/auth/reset-password",{token,password});
        if(data){
          toast.success(data || "Password updated successfully");
          navigate("/auth");
        }
      } catch (error) {
        console.log(error);
        toast.error("Failw with update password");
      }
    }

  return (
    <div className="w-full min-h-screen flex flex-col items-center justify-center p-6 bg-gray-200">
        <div className="bg-white w-full max-w-md rounded-md shadow-lg p-6">
             <h1 className="text-lg md:text-3xl font-semibold py-4 text-green-800">Update Password </h1>
             <form className="space-y-4" onSubmit={handleSubmit}>
                <div className="flex items-center gap-3 border border-gray-500 p-3 rounded-md">
                    <Lock size={20} className="text-gray-500" />
                    <input type={showPassword ? "text" : "password"} name="password" id="password" placeholder="Password" 
                    className="outline-none w-full bg-transparent" value={password} onChange={(e)=>setPassword(e.target.value)} required />
                    {showPassword ? (
                    <Eye size={20} className="text-gray-500 cursor-pointer" onClick={()=>setShowPassword(false)} />
                    
                    ) : (
                    <EyeClosed size={20} className="text-gray-500 cursor-pointer" onClick={()=>setShowPassword(true)} />
                    )}            
                </div>
                <div className="flex items-center gap-3 border border-gray-500 p-3 rounded-md">
                    <Lock size={20} className="text-gray-500" />
                    <input type={showPassword ? "text" : "password"} name="confirm-password" id="confirm-password" 
                    placeholder="Confirm Password" className="outline-none w-full bg-transparent" value={confirmPassword} onChange={(e)=>setConfirmPassword(e.target.value)} required />
                    {showPassword ? (
                    <Eye size={20} className="text-gray-500 cursor-pointer" onClick={()=>setShowPassword(false)} />
                    
                    ) : (
                    <EyeClosed size={20} className="text-gray-500 cursor-pointer" onClick={()=>setShowPassword(true)} />
                    )}            
                </div>
                <button type="submit" className="bg-green-500 text-white hover:bg-green-400 transition duration-300 w-full p-3 rounded-md shadow-md cursor-pointer" >Update Password</button>
             </form>
        </div>
    </div>
  )
}

export default ResetPassword