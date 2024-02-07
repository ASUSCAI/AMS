
import React, { useRef, useState } from 'react';
//import { useRouter } from 'next/router';
import { SplitButton } from 'primereact/splitbutton';
import { Toast } from 'primereact/toast';
import 'primereact/resources/themes/saga-blue/theme.css'; // Choose a theme
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';


export default function PairSplitButton() {
    //const router = useRouter();
    const toast = useRef(null);
    const [paired, setPaired] = useState<string>('None');
    const [defaultReader, setDefaultReader] = useState<string>('None');

    // fetch default reader for this section


    const items = [
        {
            label: 'Reader 1',
            // icon: 'pi pi-refresh',
            command: () => {
                // send request to connect to reader
                // set to defaultReader as well
                // setPaired
                setDefaultReader("Reader 1")
                setPaired("Reader 1");
                (toast.current as any)?.show({ severity: 'success', summary: 'Success', detail: 'Reader 1 Paired; Reader 1 is now Default Reader' });
            }
        },
        {
            label: 'Reader 2',
            // icon: 'pi pi-times',
            command: () => {
                // send request to connect to reader
                // set to defaultReader as well
                // setPaired
                setDefaultReader("Reader 2")
                setPaired("Reader 2");
                (toast.current as any)?.show({ severity: 'success', summary: 'Success', detail: 'Reader 2 Paired; Reader 2 is now Default Reader' });
            }
        },
        {
            label: 'Reader 3',
            // icon: 'pi pi-external-link',
            command: () => {
                // send request to connect to reader
                // set to defaultReader as well
                // setPaired
                setDefaultReader("Reader 3")
                setPaired("Reader 3");
                (toast.current as any)?.show({ severity: 'success', summary: 'Success', detail: 'Reader 3 Paired; Reader 3 is now Default Reader' });
            }
        },
        {
            label: 'Reader 4',
            // icon: 'pi pi-upload',
            command: () => {
                // send request to connect to reader
                // set to defaultReader as well
                // setPaired
                setDefaultReader("Reader 4")
                setPaired("Reader 4");
                (toast.current as any)?.show({ severity: 'success', summary: 'Success', detail: 'Reader 4 Paired; Reader 4 is now Default Reader' });
            }
        }
    ];

    const [loading, setLoading] = useState(false);

    const handlePair = () => {
        // check if defaultReader is null or not
        if (paired !== "None") {
            setPaired("None");
            // send request to disconnet reader
        }
        else{
            if (defaultReader !== "None"){
                setPaired(defaultReader);
                // send request to connect reader
                (toast.current as any)?.show({ severity: 'success', summary: 'Success', detail: defaultReader + ' Paired' });
            }
            else {
                (toast.current as any)?.show({ severity: 'warn', summary: 'No Default Reader', detail: 'Select a Reader' });
            }
        }
    };

    return (
        <div className="pair-config-btn">
            <Toast ref={toast}></Toast>
            <SplitButton label={paired === "None"? "Pair": "Paired"} icon="pi" onClick={handlePair} model={items} loading={loading} />
        </div>
    )
}
        