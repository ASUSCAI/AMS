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

export type autoGradeInfo = {
    // add more attributes moving forward
    
  };


const AutograderView = () => {

    useEffect(() => {
        //I just made this function as a basic way to get the initial values for these buttons from our backend
        //similar backend access to our save button

        const fetchCurrData = async () => {
          try {
            const response = await axios.get(process.env.NEXT_PUBLIC_URL + "/autoGradeInfo");
            const initialData = response.data;
    
            setInputPresent(initialData.inputPresent);
            setCurrPresent(initialData.inputPresent); 

            setInputLate(initialData.inputLate);
            setCurrLate(initialData.inputLate); 

            setInputAbsent(initialData.inputAbsen);
            setCurrAbsent(initialData.inputAbsent); 

            setIsEditable(false); 

          } catch (error) {
            console.error("Error fetching initial data:", error);
          }
        };
        fetchCurrData();
      }, []);


   
    const {currentDate, setCurrentDate} = useContext<DateContextType>(DateContext);
    const [currLate, setCurrLate] = useState<autoGradeInfo>();
    const [currAbsent, setCurrAbsent] = useState<autoGradeInfo>();
    const [currPresent, setCurrPresent] = useState<autoGradeInfo>();
    const [inputLate, setInputLate] = useState<autoGradeInfo>();
    const [inputAbsent, setInputAbsent] = useState<autoGradeInfo>();
    const [inputPresent, setInputPresent] = useState<autoGradeInfo>();
    const [isEditable, setIsEditable] = useState<autoGradeInfo>();

    const handleModifyRubric = () => {
        setIsEditable(true);
    };

    const handleRunAutoGrader = () => {
        

        console.log("Autograder button clicked");
        setIsEditable(false);
    };


    const handleCancel = () => {
        setInputLate(currLate);
        setInputAbsent(currAbsent);
        setInputPresent(currPresent);
        setIsEditable(false);
    };
  
    const handleSave = () => {
        setCurrLate(inputLate);
        setCurrAbsent(inputAbsent);
        setCurrPresent(inputPresent);
        const updateAutoGrader = async () => {
    
          await axios.put(process.env.NEXT_PUBLIC_URL + "/autoGradeInfo/" + inputLate,inputAbsent,inputPresent)
            .then((response) => {
              console.log("auto grader updated:", response.data);
            })
            .catch((error) => {
              console.error("Error updating auto grader:", error);
            });
        };
        updateAutoGrader();
        setIsEditable(false);
       
    };

    const handleInputChangePresent = (event) => {
        setInputPresent(event.target.value);
    };

    const handleInputChangeLate = (event) => {
        setInputLate(event.target.value);
    };
    const handleInputChangeAbsent = (event) => {
        setInputAbsent(event.target.value);
    };


   
    

   


    return (
        <div className="mx-32">
        <DateSelect
          currentDate={currentDate}
          setCurrentDate={setCurrentDate}
          disabled={false}
      
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
        <input 
        type="text"  
        value={inputPresent}
        onChange={handleInputChangePresent}
        disabled={!isEditable}
        />
        </div>
        <div className="colored-box red">
        <label>Late</label>
        <input 
        type="text"
        value={inputLate}
        onChange={handleInputChangeLate}
        disabled={!isEditable}
        />
        </div>
        <div className="colored-box red">
        <label>Absent</label>
        <input 
        type="text" 
        value={inputAbsent}
        disabled={!isEditable}
        onChange={handleInputChangeAbsent}/>
        </div>
        <View display="inline-block" padding="small" height="8rem">
            <button
            className="filter-btn btn-cancel"
            onClick={handleCancel}
            disabled={!isEditable}
            >
            Cancel
            </button>
            <button
            className="filter-btn btn-save"
            onClick={handleSave}
            disabled={!isEditable}
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