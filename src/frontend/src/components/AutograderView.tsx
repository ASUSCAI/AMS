import React, {
    useEffect,
    useState,
    useContext,
    ChangeEvent,
} from "react";
import {
    Pagination,
    ToggleGroup,
    View,
    TextInput,
    TimeSelect,
    Button,
} from "@instructure/ui";
import axios from "axios";

import type {DateContextType} from "@/contexts/DateContext";
import {DateContext} from "@/contexts/DateContext";


// helper functions
import {DateSelect} from "./DateSelect";




const AutograderView = () => {
   
    const {currentDate, setCurrentDate} = useContext<DateContextType>(DateContext);
   

    const handleModifyRubric = () => {
        

        console.log("Modify Rubric button clicked");
    };

    const handleRunAutoGrader = () => {
        

        console.log("Autograder button clicked");
    };


    const handleCancel = () => {
        

        console.log("Cancel button clicked");
    };
  
    const handleSave = () => {
        

        console.log("Save button clicked");
    };
 


   
    

   

    // @ts-ignore
    return (
        <div className="mx-32">
        <DateSelect
          currentDate={currentDate}
          setCurrentDate={setCurrentDate}
          disabled={false}
          // style={{ marginLeft: '20px' }} // Adjust the value based on your preference
        />
        
        
      
    
        <View display="inline-block" padding="small" width="25rem">
            <button
            className="filter-btn btn-Mrubric"
            onClick={handleModifyRubric}
            >
            Modify Rubric
            </button>
        </View>

        
    <div className="gray-box">
        <div className="colored-box red">
        <label>Present</label>
        <input type="text"  />
        </div>
        <div className="colored-box red">
        <label>Late</label>
        <input type="text"/>
        </div>
        <div className="colored-box red">
        <label>Absent</label>
        <input type="text" />
        </div>
        <View display="inline-block" padding="small" height="8rem">
            <button
            className="filter-btn btn-cancel"
            onClick={handleCancel}
            >
            Cancel
            </button>
            <button
            className="filter-btn btn-save"
            onClick={handleSave}
            >
            Save
            </button>
        </View>
    </div>
        
        
        
        <div>
            
             <View display="inline-block" padding="small" width="25rem">
             <button
             className="filter-btn btn-RunAG"
             onClick={handleRunAutoGrader}
             >
             Run Autograder
             </button>
             </View>
        </div>
        
      

        </div>
       
        
    );
};

export default AutograderView;