import axios from "axios";
import toast from "react-hot-toast";

const axiosInstance=axios.create({
    withCredentials:true,
    baseURL: "http://localhost:8080/api"
});

axiosInstance.interceptors.request.use(
    config=>{
        const token=localStorage.getItem("accessToken");
        if(token){
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config
    },
    (error)=>Promise.reject(error)
);

axiosInstance.interceptors.response.use(
    config=>config,
    async error=>{
        const originalRequest=error.config;
        if(error?.response?.status === 401 && !originalRequest._retry){
            originalRequest._retry=true;
            try {
               const {data} = await axiosInstance.get("/auth/refresh");
               localStorage.setItem("accessToken",data.access); 
               originalRequest.headers['Authorization']=`Bearer ${data.accessToken}`;
               return axiosInstance.request(originalRequest);
            // eslint-disable-next-line @typescript-eslint/no-unused-vars
            } catch (error) {
                toast.error("Network Error");
                localStorage.clear();
                window.location.href="/auth";
            }
        }
    }
);

export default axiosInstance;