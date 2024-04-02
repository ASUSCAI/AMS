
import React, { useRef, useState, useEffect} from 'react';
//import { useRouter } from 'next/router';
import { SplitButton } from 'primereact/splitbutton';
import { Toast } from 'primereact/toast';
import 'primereact/resources/themes/saga-blue/theme.css'; // Choose a theme
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

export default function PairSplitButton() {
    //const router = useRouter();
    const toast = useRef<Toast>(null);
    const [paired, setPaired] = useState<string>('None');
    const [defaultReader, setDefaultReader] = useState<string>('None');
    const [pairingTime, setPairingTime] = useState<null | Date>(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        let timer: NodeJS.Timeout | null = null;
    
        if (paired !== 'None') {
            setPairingTime(new Date());
    
            timer = setTimeout(() => {
                if (toast.current) {
                    setPaired('None');
                    setDefaultReader('None');
                    setPairingTime(null);
                    toast.current.show({ severity: 'info', summary: 'Auto Unpair', detail: 'Reader has been automatically unpaired after 15 minutes.' });
                }
            }, 900000); 
        }
            return () => {
            if (timer) {
                clearTimeout(timer);
            }
        };
    }, [paired]);


    // const items = [
    //     {
    //         label: 'Reader 1',
    //         // icon: 'pi pi-refresh',
    //         command: () => {
    //             // send request to connect to reader
    //             // set to defaultReader as well
    //             // setPaired
    //             setDefaultReader(defaultReader === "Reader 1" ? "None" : "Reader 1");
    //             setPaired(defaultReader === "Reader 1" ? "None" : "Reader 1");
    //             (toast.current as any)?.show({ severity: 'success', summary: 'Success', detail: 'Reader 1 Paired; Reader 1 is now Default Reader' });
    //         }
    //     },
    //     {
    //         label: 'Reader 2',
    //         // icon: 'pi pi-times',
    //         command: () => {
    //             // send request to connect to reader
    //             // set to defaultReader as well
    //             // setPaired
    //             setDefaultReader(defaultReader === "Reader 2" ? "None" : "Reader 2");
    //         setPaired(defaultReader === "Reader 2" ? "None" : "Reader 2");
    //             (toast.current as any)?.show({ severity: 'success', summary: 'Success', detail: 'Reader 2 Paired; Reader 2 is now Default Reader' });
    //         }
    //     },
    //     {
    //         label: 'Reader 3',
    //         // icon: 'pi pi-external-link',
    //         command: () => {
    //             // send request to connect to reader
    //             // set to defaultReader as well
    //             // setPaired
    //             setDefaultReader(defaultReader === "Reader 3" ? "None" : "Reader 3");
    //         setPaired(defaultReader === "Reader 3" ? "None" : "Reader 3");
    //             (toast.current as any)?.show({ severity: 'success', summary: 'Success', detail: 'Reader 3 Paired; Reader 3 is now Default Reader' });
    //         }
    //     },
    //     {
    //         label: 'Reader 4',
    //         // icon: 'pi pi-upload',
    //         command: () => {
    //             // send request to connect to reader
    //             // set to defaultReader as well
    //             // setPaired
    //             setDefaultReader(defaultReader === "Reader 4" ? "None" : "Reader 4");
    //             setPaired(defaultReader === "Reader 4" ? "None" : "Reader 4");
    //             (toast.current as any)?.show({ severity: 'success', summary: 'Success', detail: 'Reader 4 Paired; Reader 4 is now Default Reader' });
    //         }
    //     }
    // ];
    //const [loading, setLoading] = useState(false);

    const items = [
        { label: 'Reader 1', command: () => pairReader('Reader 1') },
        { label: 'Reader 2', command: () => pairReader('Reader 2') },
        { label: 'Reader 3', command: () => pairReader('Reader 3') },
        { label: 'Reader 4', command: () => pairReader('Reader 4') },
    ];

    const pairReader = (reader: string) => {
        setDefaultReader(reader);
        setPaired(reader);
        toast.current?.show({ severity: 'success', summary: 'Success', detail: `${reader} Paired; ${reader} is now Default Reader` });
    };


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
        if (paired !== 'None') {
            setPairingTime(null);
        }
    };

    return (
        <div className="pair-config-btn">
            <Toast ref={toast}></Toast>
            <SplitButton
                label={paired === "None" ? "Pair" : "Paired: " + paired}
                onClick={handlePair}
                model={items}
                loading={loading}
                className={`${paired !== "None" ? 'p-button-success paired' : ''} mb-2`}
            />
        </div>
    )
}
        