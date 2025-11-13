import { Mail } from "lucide-react"
import React, { useState } from "react";
import toast from "react-hot-toast";
import { useNavigate } from "react-router-dom"
import axiosInstance from "../api";

const ForgotPassword = () => {
    const navigate=useNavigate();
    const [email,setEmail]=useState('');

    const handleSubmit = async (e:React.FormEvent<HTMLFormElement>) =>{
        e.preventDefault();
        try {
            const {data} = await axiosInstance.post("/auth/forgot-password",{email});
            if(data){
                toast.success(data);
            }
        } catch (error) {
            console.log(error);
            toast.error("Failed with forgot password");
        }
    }

  return (
    <div>
        <div className="w-full min-h-screen flex flex-col items-center justify-center p-6 bg-gray-200">
            <div className="bg-white w-full max-w-md rounded-md shadow-lg p-6">
                <h1 className="text-lg md:text-3xl font-semibold py-4 text-green-800">Fogot Password </h1>
                <p className="pb-4 text-sm text-gray-500">Enter your email and check your mailbox.</p>
                <form className="space-y-4" onSubmit={handleSubmit}>
                    <div className="flex items-center gap-3 border border-gray-500 p-3 rounded-md">
                        <Mail size={20} className="text-gray-500" />
                        <input type="email" name="email" id="email" placeholder="Email" 
                        className="outline-none w-full bg-transparent" value={email} onChange={(e)=>setEmail(e.target.value)} />
                    </div>
                    <p className="text-sm text-gray-500 cursor-pointer hover:underline " onClick={()=>navigate("/auth")} >Go back</p>
                    <button type="submit" className="bg-green-500 text-white hover:bg-green-400 transition duration-300 w-full p-3 rounded-md shadow-md cursor-pointer" >Forgot Password</button>
                </form>
            </div>
        </div>
    </div>
  )
}

export default ForgotPassword