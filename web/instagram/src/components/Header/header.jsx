import React from "react";
import "./Header.css";
import { useHistory } from "react-router-dom";



const isAuthenticated = !!localStorage.getItem("token");
  

    

class Header extends React.Component {

    constructor(props) {
      super(props)
      this.state = {
        searchValue : "",
      }
      
    }  

    onChange = e =>  {
        this.setState({searchValue: e.target.searchValue});

    }

    busqueda (searchValue){
        this.props.history.push(`http://localhost:3000/search?q='${searchValue}`)
    }

    render() {
        const {searchValue} = this.state;
        return (
            <header>
                <nav className = "navbar navbar-default navbar-fixed-top">
                    <div className = "container-fluid">
                        <div className ="navbar-header">

                            <a className="Nav-brand-logo" href="/timeline">
                                    Instagram
                            </a>
                            
                            {isAuthenticated && (
                                <div> 
                                    <div className = "form-group">
                                        <input 
                                            type = "text" 
                                            className ="form-control" 
                                            placeholder="Search" 
                                            value={this.state.searchValue}
                                            onChange = {this.onChange}>
                                        </input>
                                    </div>
    
                                    <div className="buttonContainer text-right">
                                        <button type="text" 
                                                className="btn btn-primary" 
                                                disabled = {this.state.searchValue === ""} 
                                                onClick= {
                                                    (searchValue) => this.busqueda(searchValue)     
                                                }
                                                >
                                                Search
                                        </button>
                                    </div> 
                                </div>  
                            )}
                        </div>
                    </div>
                </nav>
            </header>         
        );
    }   
} 

export default Header;