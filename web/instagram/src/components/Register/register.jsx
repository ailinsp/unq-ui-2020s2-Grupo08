import React, { useState } from "react";
import { useHistory } from "react-router-dom";
import Notifications,{notify} from 'react-notify-toast';
import api from "../../Api/api"
import { Link } from 'react-router-dom';



const Register = () => {

  let myColor = { background: '#0E1717', text: "#FFFFFF" };
  const history = useHistory();
  const [data, setData] = useState({
    name: "", 
    email: "",
    password: "",
    image: ""
  });
  
  const handleInputChange = (event) => {
    setData({
      ...data,
      [event.target.name]: event.target.value,
    });
  };
  
  const handleSubmit = (event) => { 
    event.preventDefault();

    api.register(data)
      .then((response) => {
        history.push("/");
        const Successful = "Usuario " + data.email + " Registrado con exito";
        notify.show(Successful,"error",5000,myColor);})

      .catch((error) => {
        console.log("error : ",error.response.data.result);
        const errorUser = error.response.data.result;
        notify.show(errorUser,"error",5000,myColor);});
    };


  return (
    <div className="container-fluid">
      <Notifications />
      <div className="row no-gutter">
        <div className="d-none d-md-flex col-md-4 col-lg-6 bg-image"></div>
        <div className="col-md-8 col-lg-6">
          <div className="login d-flex align-items-center py-5">
            <div className="container">
              <div className="row">
                <div className="col-md-9 col-lg-8 mx-auto">
                
                <h3 className="login-heading mb-4">Complete the fields</h3>
                
                <form onSubmit={handleSubmit}>

                  <div className="form-label-group">
                    <input type="text" 
                            name="name" 
                            value={data.name} 
                            id="inputName"
                            className="form-control" 
                            onChange={handleInputChange} 
                            placeholder="Name" 
                            required autoFocus>        
                    </input>
                    <label htmlFor="inputName"> Name </label>
                  </div>
                  
                  <div className="form-label-group">
                    <input  type="email" 
                            name="email" 
                            value={data.email} 
                            id="inputEmail" 
                            className="form-control" 
                            onChange={handleInputChange} 
                            placeholder="Email address" 
                            required autoFocus>
                    </input>
                    <label htmlFor="inputEmail"> Email </label>
                  </div>
                  
                  <div className="form-label-group">
                    <input  type="password" 
                            name="password" 
                            value={data.password} 
                            id="inputPassword" 
                            className="form-control" 
                            onChange={handleInputChange} 
                            placeholder="Password" 
                            required>
                    </input>
                    <label htmlFor="inputPassword"> Password </label>
                  </div>
                  
                  <div className="form-label-group">
                    <input  type="text" 
                            name="image" 
                            value={data.image} 
                            id="inputImage" 
                            className="form-control"  
                            onChange={handleInputChange} 
                            placeholder="Image" 
                            required autoFocus>
                    </input>
                    <label htmlFor="inputImage"> Image </label>
                  </div>
                  
                  <button className="btn btn-lg btn-primary btn-block btn-login text-uppercase font-weight-bold mb-2" 
                    type="submit"> Register
                  </button>

                </form>
                
                <Link to="/login" className="btn btn-lg btn-primary btn-block btn-login text-uppercase font-weight-bold mb-2" 
                  role="button"> Back
                </Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Register;